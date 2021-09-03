package cn.org.atool.fluent.mybatis.base.provider;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BatchCrudImpl;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.mapper.MapperSql;
import cn.org.atool.fluent.mybatis.metadata.DbType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.base.model.InsertList.el;
import static java.util.stream.Collectors.toList;

/**
 * oracle批量插入语法
 *
 * @author wudarui
 */
public class OracleSqlKit extends CommonSqlKit {
    public OracleSqlKit(DbType dbType) {
        super(dbType);
    }

    @Override
    public String batchCrud(BatchCrudImpl crud) {
        String sql = crud.batchSql();
        return wrapperBeginEnd(sql);
    }

    @Override
    public <E extends IEntity> String insertEntity(SqlProvider provider, String prefix, E entity, boolean withPk) {
        withPk = notBlank(dbType.feature.getSeq());
        return super.insertEntity(provider, prefix, entity, withPk);
    }

    /**
     * https://blog.csdn.net/w_y_t_/article/details/51416201
     * <p>
     * https://www.cnblogs.com/xunux/p/4882761.html
     * <p>
     * https://blog.csdn.net/weixin_41175479/article/details/80608512
     */
    @Override
    public <E extends IEntity> String insertBatch(SqlProvider provider, List<E> entities, boolean withPk) {
        MapperSql sql = new MapperSql();
        List<Map> maps = this.toMaps(provider, entities, withPk);
        String tableName = dynamic(entities.get(0), provider.tableName());
        /* 所有非空字段 */
        List<FieldMapping> nonFields = this.nonFields(provider, maps, withPk);

        sql.INSERT_INTO(tableName == null ? provider.tableName() : tableName);
        sql.INSERT_COLUMNS(provider.dbType(), nonFields.stream().map(f -> f.column).collect(toList()));
        sql.APPEND("SELECT");
        if (!withPk) {
            sql.APPEND(getSeq(dbType.feature.getSeq()) + ",");
        }
        sql.APPEND("TMP.* FROM (");
        for (int index = 0; index < maps.size(); index++) {
            if (index > 0) {
                sql.APPEND("UNION ALL");
            }
            sql.APPEND("SELECT");
            boolean first = true;
            for (FieldMapping f : nonFields) {
                if (!first) {
                    sql.APPEND(", ");
                } else {
                    first = false;
                }
                sql.APPEND(el("list[" + index + "].", f, maps.get(index).get(f.column), f.insert));
            }
            sql.APPEND(" FROM dual");
        }
        sql.APPEND(") TMP");
        return sql.toString();
    }


    @Override
    public String updateBy(SqlProvider provider, IUpdate[] updaters) {
        String sql = super.updateBy(provider, updaters);
        return updaters.length == 1 ? sql : wrapperBeginEnd(sql);
    }

    public static String wrapperBeginEnd(String sql) {
        return "BEGIN " + sql + "; END;";
    }

    /**
     * 返回seq的值
     */
    private static String getSeq(String seq) {
        if (isBlank(seq)) {
            return "SEQ_USER_ID.nextval";
        }
        if (!SEQs.containsKey(seq)) {
            synchronized (SEQs) {
                String upper = seq.toUpperCase().trim();
                int index = upper.indexOf("FROM");
                if (index > 0 && upper.startsWith("SELECT") && upper.endsWith("DUAL")) {
                    SEQs.put(seq, seq.substring(6, index).trim());
                } else {
                    SEQs.put(seq, seq);
                }
            }
        }
        return SEQs.get(seq);
    }

    static final Map<String, String> SEQs = new HashMap<>();
}