package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.model.StatementType;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.util.MybatisUtil;

import java.util.Collection;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_InsertBatch;
import static cn.org.atool.fluent.mybatis.util.Constants.COMMA;

/**
 * InsertBatch: 批量插入实现
 *
 * @author darui.wu
 */
public class InsertBatch extends AbstractMethod {
    @Override
    public String statementId() {
        return Method_InsertBatch;
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {
        String values = this.getInsertValues(table);
        SqlBuilder builder = SqlBuilder.instance();
        String xml = builder.begin(StatementType.insert, statementId(), Collection.class, int.class)
            .insert(table, super.isSpecTable())
            .append("(").append(super.getColumnsWithPrimary(table)).append(")")
            .append("VALUES ")
            .foreach("list", "item", ",", () -> builder.brackets(values))
            .end(StatementType.insert)
            .toString();
        return xml;
    }

    /**
     * 构建要插入的值sql片段
     *
     * @param table
     * @return
     */
    private String getInsertValues(TableMeta table) {
        SqlBuilder values = SqlBuilder.instance();
        if (table.getPrimary() != null) {
            values.value("#{item.@property},", table.getKeyProperty(), table.getKeyColumn());
        }
        return values.eachJoining(table.getFields(), (field) -> {
            String insert = field.getInsert();
            if (MybatisUtil.isEmpty(insert)) {
                values.value("#{item.@property},", field.getProperty(), field.getColumn());
            } else {
                values.choose("item.@property != null", "#{item.@property},", insert + COMMA,
                    field.getProperty(), insert);
            }
        }).toString();
    }
}