package cn.org.atool.fluent.mybatis.functions;

import java.util.function.Function;

/**
 * 入参和出参是同一个对象的Function
 *
 * @param <R> 返回值类型
 * @author wudarui
 */
@FunctionalInterface
public interface QFunction<R> extends Function<R, R> {
}
