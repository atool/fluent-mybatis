package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.model.StatementType;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;

import java.util.Collection;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_SelectByIds;

/**
 * 按id列表查询
 *
 * @author darui.wu
 */
public class SelectByIds extends AbstractMethod {
    @Override
    public String statementId() {
        return Method_SelectByIds;
    }

    @Override
    public String getMethodSql(Class entity, TableInfo table) {
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