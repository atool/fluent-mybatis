package cn.org.atool.fluent.form.meta.entry;

import java.util.function.Function;

/**
 * MethodMetaFunc
 *
 * @author wudarui
 */
@FunctionalInterface
public interface ArgumentGetter extends Function<Object[], Object> {
    @Override
    Object apply(Object[] args);
}