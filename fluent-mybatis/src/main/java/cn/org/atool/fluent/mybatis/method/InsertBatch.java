package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.util.MybatisInsertUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import static cn.org.atool.fluent.mybatis.method.model.SqlBuilder.safeParam;
import static java.lang.String.format;

/**
 * InsertBatch: 批量插入实现
 *
 * @author darui.wu
 */
public class InsertBatch extends BaseMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo table) {
        MapperParam mapper = MapperParam.insertMapperParam(mapperClass, "insertBatch")
            .setSql(getMethodSql(table));

        return super.addMappedStatement(mapper);
    }

    @Override
    protected String getMethodSql(TableInfo table) {
        String values = this.getInsertValues(table);
        SqlBuilder builder = SqlBuilder.instance();
        return builder.beginScript()
            .insert(table.getTableName())
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
            if (MybatisInsertUtil.isInsertDefaultField(field)) {
                values.choose("item.@property != null", "#{item.@property},", field.getUpdate() + SqlBuilder.COMMA,
                    field.getProperty(), field.getUpdate());
            } else {
                values.value("#{item.@property},", field.getProperty(), field.getColumn());
            }
        }).toString();
    }
}