package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.model.StatementType;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;

import java.util.Collection;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_DeleteByIds;

/**
 * DeleteByIds: 按id列表批量删除
 *
 * @author wudarui
 */
public class DeleteByIds extends AbstractMethod {

    @Override
    public String statementId() {
        return Method_DeleteByIds;
    }

    @Override
    public String getMethodSql(Class entity, TableInfo table) {
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