package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.method.model.StatementType;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_DeleteById;

/**
 * DeleteById: 按主键删除
 *
 * @author wudarui
 */
public class DeleteById extends AbstractMethod {
    public DeleteById(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return Method_DeleteById;
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {

        SqlBuilder builder = SqlBuilder.instance();
        String xml = builder
            .begin(StatementType.delete, statementId(), table.getKeyType())
            .delete(table, super.isSpecTable())
            .where(() -> super.whereById(table, builder))
            .end(StatementType.delete)
            .toString();
        return xml;
    }
}