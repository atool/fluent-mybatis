package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.method.model.StatementType;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_Delete;

/**
 * 物理删除逻辑
 *
 * @author wudarui
 */
public class Delete extends AbstractMethod {
    public Delete(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return Method_Delete;
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {
        SqlBuilder builder = SqlBuilder.instance();
        return builder
            .begin(StatementType.delete, statementId(), entity)
            .checkWrapper()
            .delete(table, super.isSpecTable())
            .where(() -> super.whereByWrapper(builder))
            .append(() -> lastByWrapper(builder, true))
            .end(StatementType.delete)
            .toString();
    }
}