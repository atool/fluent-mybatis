package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.method.Delete;
import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import com.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import static cn.org.atool.fluent.mybatis.method.model.MethodId.Method_DeleteSpec;

/**
 * DeleteInPartition: 分库删除
 *
 * @author darui.wu
 */
public class DeleteSpec extends Delete {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo table) {
        MapperParam mapper = MapperParam.insertMapperParam(mapperClass, Method_DeleteSpec)
            .setParameterType(modelClass)
            .setResultType(Integer.class)
            .setSql(this.getMethodSql(table));
        return super.addMappedStatement(mapper);
    }

    @Override
    protected boolean isSpecTable() {
        return true;
    }
}