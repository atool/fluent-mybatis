package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.model.StatementId;
import cn.org.atool.fluent.mybatis.method.model.StatementType;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;

import java.util.Map;

/**
 * DeleteByMap: 按map的key-value删除数据
 *
 * @author wudarui
 */
public class DeleteByMap extends AbstractMethod {
    public DeleteByMap(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return StatementId.Method_DeleteByMap;
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {
        SqlBuilder builder = SqlBuilder.instance();
        String xml = builder
            .begin(StatementType.delete, statementId(), Map.class)
            .delete(table, super.isSpecTable())
            .where(() -> super.whereByMap(table, builder))
            .end(StatementType.delete)
            .toString();
        return xml;
    }
}