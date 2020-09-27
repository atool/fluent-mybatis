package cn.org.atool.fluent.mybatis.test.method.normal;

import cn.org.atool.fluent.mybatis.test.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.test.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.test.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.test.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.test.method.model.StatementType;

import java.util.Collection;

import static cn.org.atool.fluent.mybatis.test.method.model.StatementId.Method_DeleteByIds;

/**
 * DeleteByIds: 按id列表批量删除
 *
 * @author wudarui
 */
public class DeleteByIds extends AbstractMethod {
    public DeleteByIds(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return Method_DeleteByIds;
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {
        SqlBuilder builder = SqlBuilder.instance();
        String xml = builder
            .begin(StatementType.delete, statementId(), Collection.class)
            .delete(table, super.isSpecTable())
            .where(() -> this.whereByIds(table, builder))
            .end(StatementType.delete)
            .toString();
        return xml;
    }
}