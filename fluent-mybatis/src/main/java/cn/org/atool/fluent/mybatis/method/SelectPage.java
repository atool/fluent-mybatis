package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import static cn.org.atool.fluent.mybatis.method.model.MethodId.Method_SelectPage;

/**
 * SelectPage: 查询满足条件所有数据（并翻页）
 *
 * @author darui.wu
 * @create 2020/5/18 3:07 下午
 */
public class SelectPage extends SelectList {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        MapperParam mapper = MapperParam.queryMapperParam(mapperClass, Method_SelectPage)
            .setResultType(modelClass)
            .setSql(this.getMethodSql(tableInfo));

        return super.addMappedStatement(mapper);
    }
}