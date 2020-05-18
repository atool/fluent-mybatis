package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import static cn.org.atool.fluent.mybatis.method.model.MethodId.Method_UpdateById;

/**
 * 根据 ID 更新有值字段
 *
 * @author darui.wu
 * @create 2020/1/2 5:30 下午
 */
public class UpdateById extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        MapperParam mapper = MapperParam.updateMapperParam(mapperClass, Method_UpdateById)
            .setSql(this.getMethodSql(tableInfo));
        return super.addMappedStatement(mapper);
    }

    @Override
    protected String getMethodSql(TableInfo table) {
        SqlBuilder builder = SqlBuilder.instance();
        return builder.beginScript()
            .update(table, super.isSpecTable())
            .set(() -> builder.eachJoining(table.getFieldList(), (field) -> updateField(builder, field)))
            .where(() -> super.whereById(table, builder))
            .endScript();
    }

    private void updateField(SqlBuilder builder, TableFieldInfo field) {
        if (isUpdateDefault(field)) {
            builder.value("@column=@property,", field.getUpdate(), field.getColumn());
        } else {
            builder.ifThen("et.@property != null", "@column=#{et.@property},", field.getProperty(), field.getColumn());
        }
    }
}