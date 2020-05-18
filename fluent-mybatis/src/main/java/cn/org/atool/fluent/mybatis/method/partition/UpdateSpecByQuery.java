package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.method.UpdateByQuery;
import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import com.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import static cn.org.atool.fluent.mybatis.method.model.MethodId.Method_UpdateSpecByQuery;

/**
 * UpdateSpecByQuery : 更新指定分表
 *
 * @author darui.wu
 */
public class UpdateSpecByQuery extends UpdateByQuery {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        MapperParam mapper = MapperParam.updateMapperParam(mapperClass, Method_UpdateSpecByQuery)
            .setParameterType(modelClass)
            .setSql(this.getMethodSql(tableInfo));

        return super.addMappedStatement(mapper);
    }

    @Override
    protected boolean isSpecTable() {
        return true;
    }
}