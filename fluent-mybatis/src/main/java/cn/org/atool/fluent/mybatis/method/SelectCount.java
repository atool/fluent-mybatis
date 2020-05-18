package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import static cn.org.atool.fluent.mybatis.method.model.MethodId.Method_SelectCount;

/**
 * SelectCount: 查询满足条件总记录数
 *
 * @author darui.wu
 * @create 2020/5/18 11:42 上午
 */
public class SelectCount extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        MapperParam mapper = MapperParam.queryMapperParam(mapperClass, Method_SelectCount)
            .setResultType(Integer.class)
            .setSql(this.getMethodSql(tableInfo));

        return super.addMappedStatement(mapper);
    }

    @Override
    protected String getMethodSql(TableInfo table) {
        SqlBuilder builder = SqlBuilder.instance();
        return builder.beginScript()
            .selectCount(table, super.isSpecTable())
            .where(() -> super.whereEntity(table, builder))
            .suffixComment()
            .endScript();
    }
}