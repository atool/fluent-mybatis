package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.method.model.MethodId.Method_SelectMapsPage;

/**
 * SelectMapsPage: 分页查询
 *
 * @author darui.wu
 * @create 2020/5/18 3:32 下午
 */
public class SelectMapsPage extends SelectMaps {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        MapperParam mapper = MapperParam.queryMapperParam(mapperClass, Method_SelectMapsPage)
            .setResultType(Map.class)
            .setSql(this.getMethodSql(tableInfo));

        return super.addMappedStatement(mapper);
    }
}