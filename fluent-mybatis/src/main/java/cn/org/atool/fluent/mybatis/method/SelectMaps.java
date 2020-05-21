package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.method.model.MethodId.Method_SelectMaps;

/**
 * SelectMaps:  查询满足条件所有数据
 *
 * @author darui.wu
 * @create 2020/5/18 1:45 下午
 */
public class SelectMaps extends SelectList {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        MapperParam mapper = MapperParam.queryMapperParam(mapperClass, Method_SelectMaps)
            .setResultType(Map.class)
            .setSql(this.getMethodSql(tableInfo));

        return super.addMappedStatement(mapper);
    }
}