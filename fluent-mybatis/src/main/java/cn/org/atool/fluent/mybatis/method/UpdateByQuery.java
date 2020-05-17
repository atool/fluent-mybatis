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
            .where(() -> super.where(table, builder))
            .append(() -> super.comment(builder))
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
            builder.value("@column=@property,", field.getUpdate(), field.getColumn());
        } else {
            builder.ifThen("ew.updates.containsKey('@property')", "@column=#{ew.updates.@property},", field.getProperty(), field.getColumn());
        }
    }
}