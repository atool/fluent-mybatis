package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.method.SelectList;
import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import com.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import static cn.org.atool.fluent.mybatis.method.model.MethodId.Method_SelectListFromSpec;

/**
 * SelectSpec : 从指定表（分表）查询
 *
 * @author darui.wu
 */
public class SelectSpec extends SelectList {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        MapperParam mapper = MapperParam.queryMapperParam(mapperClass, Method_SelectListFromSpec)
            .setResultType(modelClass)
            .setSql(this.getMethodSql(tableInfo));

        return super.addMappedStatement(mapper);
    }

    @Override
    protected boolean isSpecTable() {
        return true;
    }
}