package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.metadata.FieldMeta;

import java.util.function.Predicate;

/**
 * FieldPredicate: 对判断字段是否符合条件
 *
 * @author wudarui
 */
@FunctionalInterface
public interface FieldPredicate extends Predicate<FieldMeta> {
}