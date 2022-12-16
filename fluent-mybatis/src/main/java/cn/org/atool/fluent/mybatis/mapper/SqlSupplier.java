package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.apache.ibatis.builder.annotation.ProviderContextKit.getProviderContext;
import static org.apache.ibatis.builder.annotation.ProviderContextKit.getSqlSupplier;

/**
 * 根据mapping定义, 返回sql方法对于的sql语句构造器
 *
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
@FunctionalInterface
public interface SqlSupplier extends Function<Map, String> {
    static SqlSupplier get(IMapping mapping, String method) {
        ProviderContext context = getProviderContext(mapping.mapperClass(), method);
        SqlSupplierByProviderContext supplier = getSqlSupplier(method);
        return map -> supplier.apply(map, context);
    }

    @FunctionalInterface
    interface SqlSupplierByProviderContext extends BiFunction<Map, ProviderContext, String> {
    }
}
