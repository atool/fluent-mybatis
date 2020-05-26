package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.model.StatementType;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_SelectList;

/**
 * SelectList: 查询满足条件所有数据
 *
 * @author darui.wu
 * @create 2020/5/18 12:07 下午
 */
public class SelectList extends AbstractMethod {

    @Override
    public String statementId() {
        return Method_SelectList;
    }

    @Override
    public String getMethodSql(Class entity, TableInfo table) {
        SqlBuilder builder = SqlBuilder.instance();
        String xml = builder
            .begin(StatementType.select, statementId(), Map.class, resultType())
            .select(table, true, super.isSpecTable())
            .where(() -> super.whereEntity(table, builder))
            .suffixComment()
            .end(StatementType.select)
            .toString();
        return xml;
    }

    /**
     * 这里单独定义出来，方便重载
     *
     * @return
     */
    protected Class resultType() {
        return null;
    }
}