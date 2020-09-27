package cn.org.atool.fluent.mybatis.test.method.normal;

import cn.org.atool.fluent.mybatis.test.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.test.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.test.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.test.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.test.method.model.StatementType;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.test.method.model.StatementId.Method_SelectCount;

/**
 * SelectCount: 查询满足条件总记录数
 *
 * @author darui.wu
 * @create 2020/5/18 11:42 上午
 */
public class SelectCount extends AbstractMethod {
    public SelectCount(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return Method_SelectCount;
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {
        String noPageSelect = this.noPagedXml(table);

        SqlBuilder builder = SqlBuilder.instance();
        String xml = builder
            .begin(StatementType.select, statementId(), Map.class, int.class)
            .checkWrapper()
            .choosePaged(noPageSelect, super.getDbType().selectByPaged(noPageSelect))
            .end(StatementType.select)
            .toString();

        return xml;
    }

    protected String noPagedXml(TableMeta table) {
        SqlBuilder builder = SqlBuilder.instance();
        return builder.selectCount(table, super.isSpecTable())
            .where(() -> super.whereByWrapper(builder))
            .append(() -> lastByWrapper(builder, true))
            .toString();
    }
}