package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import static cn.org.atool.fluent.mybatis.method.model.MethodId.Method_Insert;
import static cn.org.atool.fluent.mybatis.method.model.SqlBuilder.COMMA;

/**
 * InsertSelected 插入非null的字段，忽略null字段
 *
 * @author darui.wu
 * @create 2020/5/12 8:51 下午
 */
public class Insert extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo table) {
        MapperParam mapper = MapperParam.insertMapperParam(mapperClass, Method_Insert)
            .setParameterType(modelClass)
            .setResultType(Integer.class)
            .setSql(this.getMethodSql(table));
        super.setKeyGenerator(mapper, table);
        return super.addMappedStatement(mapper);
    }

    /**
     * <pre>
     *      INSERT INTO table_name
     * 		<trim prefix="(" suffix=")" suffixOverrides=",">
     * 			<if test="ew.field != null">
     * 				field,
     * 			</if>
     * 		</trim>
     * 		<trim prefix="values (" suffix=")" suffixOverrides=",">
     * 			<if test="ew.field != null">
     * 				#{field},
     * 			</if>
     * 		</trim>
     * 	</pre>
     *
     * @param table 表结构
     * @return
     */
    @Override
    protected String getMethodSql(TableInfo table) {
        SqlBuilder builder = SqlBuilder.instance();
        return builder.beginScript()
            .insert(table, super.isSpecTable())
            .brackets(() -> {
                builder
                    .ifThen("@property != null", "@column,", table.getKeyProperty(), table.getKeyColumn())
                    .eachJoining(table.getFieldList(), (field) -> field(builder, field));
            })
            .append("VALUES")
            .brackets(() -> {
                builder
                    .ifThen("@property != null", "#{@property},", table.getKeyProperty(), table.getKeyColumn())
                    .eachJoining(table.getFieldList(), (field) -> value(builder, field));
            })
            .endScript();
    }

    private void value(SqlBuilder builder, TableFieldInfo field) {
        if (isInsertDefault(field)) {
            builder.choose("@property != null", "#{@property},", field.getUpdate() + COMMA,
                field.getProperty(), field.getColumn());
        } else {
            builder.ifThen("@property != null", "#{@property},", field.getProperty(), field.getColumn());
        }
    }

    private void field(SqlBuilder builder, TableFieldInfo field) {
        if (isInsertDefault(field)) {
            builder.append(field.getColumn() + COMMA);
        } else {
            builder.ifThen("@property != null", "@column,", field.getProperty(), field.getColumn());
        }
    }
}