package cn.org.atool.fluent.mybatis.base.provider;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.mapper.MapperSql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.If.isBlank;

/**
 * oracle批量插入语法
 *
 * @author wudarui
 */
public class OracleSqlProvider {
    public static <E extends IEntity> String insertBatch(BaseSqlProvider provider, List<E> entities, boolean withPk) {
        MapperSql sql = new MapperSql();
        String tableName = null;
        for (IEntity entity : entities) {
            provider.validateInsertEntity(entity, withPk);
            if (tableName == null) {
                tableName = provider.dynamic(entity);
            }
        }
        sql.INSERT_INTO(tableName == null ? provider.tableName() : tableName);
        sql.INSERT_COLUMNS(provider.dbType(), provider.allFields(true));
        for (int index = 0; index < entities.size(); index++) {
            if (index > 0) {
                sql.APPEND("UNION ALL");
            }
            sql.APPEND("SELECT");
            String fields = String.join(", ",
                provider.insertBatchEntity(index, entities.get(index), withPk)
            );
            if (!withPk) {
                sql.APPEND(getSeq(provider.getSeq()) + ",");
            }
            sql.APPEND(fields + " FROM dual");
        }
        return sql.toString();
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
