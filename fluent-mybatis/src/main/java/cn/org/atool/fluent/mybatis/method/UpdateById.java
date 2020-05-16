package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.util.MybatisInsertUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 根据 ID 更新有值字段
 *
 * @author darui.wu
 * @create 2020/1/2 5:30 下午
 */
public class UpdateById extends BaseMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        MapperParam mapper = MapperParam.updateMapperParam(mapperClass, "updateById")
            .setSql(this.getMethodSql(tableInfo));
        return super.addMappedStatement(mapper);
    }

    @Override
    protected String getMethodSql(TableInfo table) {
        SqlBuilder builder = SqlBuilder.instance();
        return builder.beginScript()
            .update(table.getTableName())
            .set(() -> builder.eachJoining(table.getFieldList(), (field) -> updateField(builder, field)))
            .where(() -> builder.andVariable(table.getKeyColumn(), ENTITY_DOT, table.getKeyProperty()))
            .endScript();
    }

    private void updateField(SqlBuilder builder, TableFieldInfo field) {
        if (MybatisInsertUtil.isUpdateDefaultField(field)) {
            builder.setValue(field.getColumn(), field.getUpdate());
        } else {
            builder.ifThen(
                String.format("et.%s != null", field.getProperty()),
                String.format("%s=#{et.%s},", field.getColumn(), field.getProperty())
            );
        }
    }
}