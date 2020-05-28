package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.model.StatementType;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_SelectById;

/**
 * SelectById: 根据ID 查询一条数据
 *
 * @author wudarui
 */
public class SelectById extends AbstractMethod {

    @Override
    public String statementId() {
        return Method_SelectById;
    }

    @Override
    public String getMethodSql(Class entity, TableInfo table) {
        SqlBuilder builder = SqlBuilder.instance();
        String xml = builder
            .begin(StatementType.select, statementId(), table.getKeyType())
            .select(table, false, super.isSpecTable())
            .where(() -> super.whereById(table, builder))
            .end(StatementType.select)
            .toString();
        return xml;
    }
}