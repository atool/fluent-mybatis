package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.model.StatementId;
import cn.org.atool.fluent.mybatis.method.model.StatementType;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;

import java.util.Collection;

/**
 * 按id列表查询
 *
 * @author darui.wu
 */
public class SelectByIds extends AbstractMethod {
    public SelectByIds(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return StatementId.Method_SelectByIds;
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {
        SqlBuilder builder = SqlBuilder.instance();
        String xml = builder
            .begin(StatementType.select, statementId(), Collection.class)
            .select(table, false, super.isSpecTable())
            .where(() -> super.whereByIds(table, builder))
            .end(StatementType.select)
            .toString();
        return xml;
    }
}