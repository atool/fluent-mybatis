package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.method.model.StatementType;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_SelectList;
import static cn.org.atool.fluent.mybatis.method.model.XmlConstant.*;

/**
 * SelectList: 查询满足条件所有数据
 *
 * @author darui.wu
 * @create 2020/5/18 12:07 下午
 */
public class SelectList extends AbstractMethod {
    public SelectList(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return Method_SelectList;
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {
        String noPageXml = this.noPageXml(table);

        SqlBuilder builder = SqlBuilder.instance();
        builder
            .begin(StatementType.select, statementId(), Map.class, resultType())
            .checkWrapper();
        if (super.getDbType().isCanDirectLimit()) {
            builder.append(noPageXml).limitDirectly();
        } else {
            builder.choosePaged(noPageXml, super.getDbType().selectByPaged(noPageXml));
        }
        String xml = builder.end(StatementType.select)
            .toString();
        return xml;
    }

    private String noPageXml(TableMeta table) {
        SqlBuilder builder = SqlBuilder.instance();
        return builder
            .select(table, true, super.isSpecTable())
            .where(() -> super.whereByWrapper(builder))
            .toString();
    }

    /**
     * 这里单独定义出来，方便重载
     *
     * @return
     */
    protected Class resultType() {
        return null;
    }
}