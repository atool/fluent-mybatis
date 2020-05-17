package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;

/**
 * DeleteByMap: 按map的key-value删除数据
 *
 * @author wudarui
 */
public class DeleteByMap extends BaseMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo table) {
        MapperParam mapper = MapperParam.insertMapperParam(mapperClass, "deleteByMap")
            .setParameterType(Map.class)
            .setResultType(Integer.class)
            .setSql(this.getMethodSql(table));
        return super.addMappedStatement(mapper);
    }

    @Override
    protected String getMethodSql(TableInfo table) {
        SqlBuilder builder = SqlBuilder.instance();
        return builder.beginScript()
            .delete(table.getTableName())
            .where(() -> this.whereMap(table, builder))
            .endScript();
    }

    private void whereMap(TableInfo table, SqlBuilder builder) {
        builder.ifThen("cm != null and !cm.isEmpty", () -> {
            builder.foreach("cm", "v", "AND ", () -> {
                builder.choose("v == null","${k} IS NULL ","${k} = #{v}");
            });
        });
    }
}
