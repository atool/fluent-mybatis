package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;

import java.util.function.Function;

/**
 * IEntity实例关联键值构造器
 *
 * @param <E> IEntity类型
 * @author darui.wu
 */
@FunctionalInterface
public interface RefKeyFunc<E extends IEntity> extends Function<E, String> {
}