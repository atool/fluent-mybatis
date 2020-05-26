package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.model.StatementType;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_Delete;

/**
 * 物理删除逻辑
 *
 * @author wudarui
 */
public class Delete extends AbstractMethod {

    @Override
    public String statementId() {
        return Method_Delete;
    }

    @Override
    public String getMethodSql(Class entity, TableInfo table) {
        SqlBuilder builder = SqlBuilder.instance();
        return builder
            .begin(StatementType.delete, statementId(), entity)
            .delete(table, super.isSpecTable())
            .where(() -> super.whereEntity(table, builder))
            .end(StatementType.delete)
            .toString();
    }
}