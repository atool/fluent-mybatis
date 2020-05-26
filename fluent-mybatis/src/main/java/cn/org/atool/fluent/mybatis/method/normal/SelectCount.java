package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.method.model.StatementType;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_SelectCount;

/**
 * SelectCount: 查询满足条件总记录数
 *
 * @author darui.wu
 * @create 2020/5/18 11:42 上午
 */
public class SelectCount extends AbstractMethod {

    @Override
    public String statementId() {
        return Method_SelectCount;
    }

    @Override
    public String getMethodSql(Class entity, TableInfo table) {
        SqlBuilder builder = SqlBuilder.instance();
        String xml = builder
            .begin(StatementType.select, statementId(), Map.class, int.class)
            .selectCount(table, super.isSpecTable())
            .where(() -> super.whereEntity(table, builder))
            .suffixComment()
            .end(StatementType.select)
            .toString();
        return xml;
    }
}