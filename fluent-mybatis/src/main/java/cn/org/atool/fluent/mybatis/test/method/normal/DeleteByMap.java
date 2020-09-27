package cn.org.atool.fluent.mybatis.test.method.normal;

import cn.org.atool.fluent.mybatis.test.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.test.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.test.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.test.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.test.method.model.StatementType;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.test.method.model.StatementId.Method_DeleteByMap;

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
        return Method_DeleteByMap;
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