package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.metadata.TablePrimaryMeta;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.method.model.StatementType;

import static cn.org.atool.fluent.mybatis.segment.model.StrConstant.COMMA;
import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_InsertBatch;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isBlank;

/**
 * InsertBatch: 批量插入实现
 *
 * @author darui.wu
 */
public class InsertBatch extends AbstractMethod {
    public InsertBatch(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return Method_InsertBatch;
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {
        SqlBuilder builder = SqlBuilder.instance();
        this.insertStatement(builder, entity, table.getPrimary());
        String values = this.getInsertValues(table);
        builder
            .insert(table, super.isSpecTable())
            .append("(").append(super.getColumnsWithPrimary(table)).append(")")
            .append("VALUES ")
            .foreach("list", "item", ",", () -> builder.brackets(values));

        String xml = builder.end(StatementType.insert).toString();
        return xml;
    }

    protected void insertStatement(SqlBuilder builder, Class entity, TablePrimaryMeta primary) {
        builder.quotas("<insert id='%s' parameterType='%s'>", statementId(), entity.getName());
    }

    /**
     * 构建要插入的值sql片段
     *
     * @param table
     * @return
     */
    protected String getInsertValues(TableMeta table) {
        SqlBuilder values = SqlBuilder.instance();
        if (table.getPrimary() != null) {
            values.value("#{item.@property},", table.getKeyProperty(), table.getKeyColumn());
        }
        values.eachJoining(table.getFields(), (field) -> {
            String insert = field.getInsert();
            if (isBlank(insert)) {
                values.value("#{item.@property},", field.getProperty(), field.getColumn());
            } else {
                values.choose("item.@property != null", "#{item.@property},", insert + COMMA,
                    field.getProperty(), insert);
            }
        });
        return values.toString();
    }
}