package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import static cn.org.atool.fluent.mybatis.util.MybatisInsertUtil.isUpdateDefaultField;
import static java.lang.String.format;

/**
 * UpdateByQuery
 *
 * @author darui.wu
 */
public class UpdateByQuery extends BaseMethod {

    private final static String MAPPER_METHOD_ID = "updateBy";

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        MapperParam mapper = MapperParam.updateMapperParam(mapperClass, MAPPER_METHOD_ID)
            .setParameterType(modelClass)
            .setSql(this.getMethodSql(tableInfo));

        return super.addMappedStatement(mapper);
    }

    @Override
    protected String getMethodSql(TableInfo table) {
        SqlBuilder builder = SqlBuilder.instance();
        return builder.beginScript()
            .update(table.getTableName())
            .set(() -> update(table, builder))
            .where(() -> where(table, builder))
            .ifThen("ew != null and ew.sqlComment != null", "${ew.sqlComment}")
            .endScript();
    }

    /**
     * update set 设置部分
     *
     * @param table
     * @param builder
     * @return
     */
    protected SqlBuilder update(TableInfo table, SqlBuilder builder) {
        return builder
            .ifThen("ew != null and ew.updates != null", () -> builder.eachJoining(table.getFieldList(), (field) -> updateField(builder, field)))
            .ifThen("ew != null and ew.sqlSet != null", "${ew.sqlSet}");
    }

    private void updateField(SqlBuilder builder, TableFieldInfo field) {
        if (isUpdateDefaultField(field)) {
            builder.append("%s=%s,", field.getColumn(), field.getUpdate());
        } else {
            builder.ifThen(
                format("ew.updates.containsKey('%s')", field.getProperty()),
                format("%s=#{ew.updates.%s},", field.getColumn(), field.getProperty())
            );
        }
    }

    /**
     * where部分
     *
     * @param table
     * @param builder
     * @return
     */
    protected SqlBuilder where(TableInfo table, SqlBuilder builder) {
        return builder
            .ifThen("ew != null and ew.entity != null",
                () -> builder.eachJoining(table.getFieldList(), (field) -> builder.ifThen(
                    format("ew.entity.%s != null", field.getProperty()),
                    format("AND %s=#{ew.entity.%s}", field.getColumn(), field.getProperty()))
                ))
            .ifThen(
                "ew != null and ew.sqlSegment != null and ew.sqlSegment != ''",
                "AND ${ew.sqlSegment}");
    }
}