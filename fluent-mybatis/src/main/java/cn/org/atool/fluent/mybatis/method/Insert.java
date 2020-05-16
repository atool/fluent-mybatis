package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.util.MybatisInsertUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import static java.lang.String.format;

/**
 * InsertSelected 插入非null的字段，忽略null字段
 *
 * @author darui.wu
 * @create 2020/5/12 8:51 下午
 */
public class Insert extends BaseMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo table) {
        MapperParam mapper = MapperParam.insertMapperParam(mapperClass, this.getMethodId())
            .setParameterType(modelClass)
            .setResultType(Integer.class)
            .setSql(this.getMethodSql(table));
        super.setPrimaryKey(mapper, table);

        super.setPrimaryKey(mapper, table);
        return super.addMappedStatement(mapper);
    }

    protected String getMethodId() {
        return "insert";
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
            .insert(table.getTableName())
            .brackets(() -> builder
                .ifThen(format("%s != null", table.getKeyProperty()), table.getKeyColumn() + COMMA)
                .eachJoining(table.getFieldList(), (field) -> field(builder, field)))
            .append("VALUES")
            .brackets(() -> builder
                .ifThen(format("%s != null", table.getKeyProperty()), format("#{%s},", table.getKeyProperty()))
                .eachJoining(table.getFieldList(), (field) -> value(builder, field)))
            .endScript();
    }

    private void value(SqlBuilder builder, TableFieldInfo field) {
        if (MybatisInsertUtil.isInsertDefaultField(field)) {
            builder.choose(
                format("%s != null", field.getProperty()),
                format("#{%s},", field.getProperty()),
                format("%s,", field.getUpdate()));
        } else {
            builder.ifThen(
                format("%s != null", field.getProperty()),
                format("#{%s},", field.getProperty()));
        }
    }

    private void field(SqlBuilder builder, TableFieldInfo field) {
        if (MybatisInsertUtil.isInsertDefaultField(field)) {
            builder.append(field.getColumn() + COMMA);
        } else {
            builder.ifThen(format("%s != null", field.getProperty()), field.getColumn() + COMMA);
        }
    }
}