package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.method.model.StatementType;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_Count_NoLimit;
import static cn.org.atool.fluent.mybatis.method.model.XmlConstant.Wrapper_Where_NoOrder_Not_Null;
import static cn.org.atool.fluent.mybatis.method.model.XmlConstant.Wrapper_Where_NoOrder_Var;

/**
 * SelectCountNoLimit: 忽略order by 和 limit 语句 查询总数
 *
 * @author darui.wu
 * @create 2020/5/18 11:42 上午
 */
public class CountNoLimit extends AbstractMethod {
    public CountNoLimit(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return Method_Count_NoLimit;
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {
        String noPageSelect = this.noPagedXml(table);

        SqlBuilder builder = SqlBuilder.instance();
        String xml = builder
            .begin(StatementType.select, statementId(), Map.class, int.class)
            .checkWrapper()
            .append(noPageSelect)
            .end(StatementType.select)
            .toString();

        return xml;
    }

    private String noPagedXml(TableMeta table) {
        SqlBuilder builder = SqlBuilder.instance();
        return builder.selectCount(table, super.isSpecTable())
            .where(() -> builder.ifThen(Wrapper_Where_NoOrder_Not_Null, Wrapper_Where_NoOrder_Var))
            .toString();
    }
}