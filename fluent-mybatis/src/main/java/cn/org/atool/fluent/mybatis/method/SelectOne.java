package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import static cn.org.atool.fluent.mybatis.method.model.MethodId.Method_SelectOne;

/**
 * SelectOne: 查询满足条件一条数据
 *
 * @author darui.wu
 * @create 2020/5/18 11:18 上午
 */
public class SelectOne extends SelectList {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        MapperParam mapper = MapperParam.queryMapperParam(mapperClass, Method_SelectOne)
            .setResultType(modelClass)
            .setSql(this.getMethodSql(tableInfo));

        return super.addMappedStatement(mapper);
    }
}