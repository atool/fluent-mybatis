package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import cn.org.atool.fluent.mybatis.util.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;

import static cn.org.atool.fluent.mybatis.method.model.MethodId.Method_InsertBatch;

/**
 * InsertBatch: 批量插入实现
 *
 * @author darui.wu
 */
public class InsertBatch extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo table) {
        MapperParam mapper = MapperParam.insertMapperParam(mapperClass, Method_InsertBatch)
            .setSql(getMethodSql(table));

        return super.addMappedStatement(mapper);
    }

    @Override
    protected String getMethodSql(TableInfo table) {
        String values = this.getInsertValues(table);
        SqlBuilder builder = SqlBuilder.instance();
        return builder.beginScript()
            .insert(table, super.isSpecTable())
            .append("(").append(super.getColumnsWithPrimary(table)).append(")")
            .append("VALUES ")
            .foreach("list", "item", ",", () -> builder.brackets(values))
            .endScript();
    }

    /**
     * 构建要插入的值sql片段
     *
     * @param table
     * @return
     */
    private String getInsertValues(TableInfo table) {
        SqlBuilder values = SqlBuilder.instance();
        if (table.getKeyProperty() != null) {
            values.value("#{item.@property},", table.getKeyProperty(), table.getKeyColumn());
        }
        return values.eachJoining(table.getFieldList(), (field) -> {
            String insert = field.getInsert();
            if (StringUtils.isEmpty(insert)) {
                values.value("#{item.@property},", field.getProperty(), field.getColumn());
            } else {
                values.choose("item.@property != null", "#{item.@property},", insert + SqlBuilder.COMMA,
                    field.getProperty(), insert);
            }
        }).toString();
    }
}