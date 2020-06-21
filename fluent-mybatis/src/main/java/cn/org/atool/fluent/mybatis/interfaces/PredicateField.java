package cn.org.atool.fluent.mybatis.interfaces;

import cn.org.atool.fluent.mybatis.method.metadata.BaseFieldMeta;

import java.util.function.Predicate;

/**
 * PredicateField
 *
 * @author wudarui
 */
@FunctionalInterface
public interface PredicateField extends Predicate<BaseFieldMeta> {
}