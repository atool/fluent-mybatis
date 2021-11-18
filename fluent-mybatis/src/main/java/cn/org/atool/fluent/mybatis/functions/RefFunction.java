package cn.org.atool.fluent.mybatis.functions;

import java.io.Serializable;
import java.util.function.Function;

/**
 * RelateFunction 关联方法
 *
 * @param <E> IEntity类型
 * @author wudarui
 */
@FunctionalInterface
public interface RefFunction<E> extends Function<E, Object>, Serializable {
}