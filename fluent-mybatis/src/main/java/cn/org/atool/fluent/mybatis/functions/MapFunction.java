package cn.org.atool.fluent.mybatis.functions;

import java.util.Map;
import java.util.function.Function;

/**
 * Map对象映射Function
 *
 * @param <R> 返回值类型
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
@FunctionalInterface
public interface MapFunction<R> extends Function<Map, R> {
}
