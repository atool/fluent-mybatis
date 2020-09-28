package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.model.StatementId;
import cn.org.atool.fluent.mybatis.method.model.StatementType;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;

import java.util.Collection;

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
        return StatementId.Method_DeleteByIds;
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