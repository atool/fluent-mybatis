package cn.org.atool.fluent.mybatis.test.method.normal;

import cn.org.atool.fluent.mybatis.test.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.test.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.test.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.test.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.test.method.model.StatementType;

import java.util.Collection;

import static cn.org.atool.fluent.mybatis.test.method.model.StatementId.Method_SelectByIds;

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
        return Method_SelectByIds;
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