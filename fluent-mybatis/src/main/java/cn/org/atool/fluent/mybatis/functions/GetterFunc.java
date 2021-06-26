package cn.org.atool.fluent.mybatis.functions;

import java.io.Serializable;
import java.util.function.Function;
/**
 * IEntity::getter()函数
 *
 * @param <E>
 * @author wudarui
 */
@FunctionalInterface
public interface GetterFunc<E> extends Serializable, Function<E, Object> {
}