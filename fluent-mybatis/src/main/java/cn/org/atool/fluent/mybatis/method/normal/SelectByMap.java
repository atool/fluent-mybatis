package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.method.model.StatementType;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_SelectByMap;

/**
 * SelectByMap: 根据map 查询数据
 *
 * @author wudarui
 */
public class SelectByMap extends AbstractMethod {
    public SelectByMap(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return Method_SelectByMap;
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {
        SqlBuilder builder = SqlBuilder.instance();
        String xml = builder
            .begin(StatementType.select, statementId(), Map.class)
            .select(table, false, super.isSpecTable())
            .where(() -> super.whereByMap(table, builder))
            .end(StatementType.select)
            .toString();
        return xml;
    }
}