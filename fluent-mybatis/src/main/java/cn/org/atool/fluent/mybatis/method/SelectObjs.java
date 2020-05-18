package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import static cn.org.atool.fluent.mybatis.method.model.MethodId.Method_SelectObjs;

/**
 * SelectObjs: 查询满足条件所有数据,只返回第一个字段的值
 *
 * @author darui.wu
 * @create 2020/5/18 2:09 下午
 */
public class SelectObjs extends SelectList {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        MapperParam mapper = MapperParam.queryMapperParam(mapperClass, Method_SelectObjs)
            .setResultType(Object.class)
            .setSql(this.getMethodSql(tableInfo));

        return super.addMappedStatement(mapper);
    }
}