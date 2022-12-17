package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.common.kits.StrKey;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.utility.LambdaUtil;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.mapper.PrinterMapper.WRAPPER_PARAMETER;

@SuppressWarnings({"rawtypes", "unchecked"})
public interface WrapperFunction<E> extends BiFunction<IEntityMapper, E, Object>, Serializable {
    @FunctionalInterface
    interface IQueryFunction extends WrapperFunction<IQuery> {
    }

    @FunctionalInterface
    interface IUpdateFunction extends WrapperFunction<IUpdate> {
    }

    static StrKey<Map<String, Object>> sql(IWrapper wrapper, WrapperFunction wrapperFunction) {
        AMapping mapping = (AMapping) wrapper.mapping().orElseGet(() -> new RuntimeException("IMapping not found."));
        PrinterMapper mapper = (PrinterMapper) PrinterMapper.set(2, mapping);
        wrapperFunction.apply(mapper, wrapper);
        String sql = mapper.getSql().get(0);
        String method = LambdaUtil.lambdaName(wrapperFunction);
        Function<Object, Map> function = WRAPPER_PARAMETER.get(method);
        Map<String, Object> data = function.apply(wrapper);
        return new StrKey<>(sql, data);
    }
}
