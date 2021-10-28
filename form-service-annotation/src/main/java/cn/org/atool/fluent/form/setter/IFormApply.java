package cn.org.atool.fluent.form.setter;

import cn.org.atool.fluent.mybatis.functions.IGetter;

/**
 * 简单表单查询
 * 简单表单查询
 *
 * @author wudarui
 */
@SuppressWarnings({"unused"})
public interface IFormApply {

    /**
     * 设置更新字段
     *
     * @param getter 字段
     * @return ignore
     */
    <E> IFormApply and(IGetter<E> getter);

    /**
     * 设置更新字段
     *
     * @param field 字段
     * @return ignore
     */
    IFormApply and(String field);

    <E> IFormApply eq(IGetter<E> getter);

    IFormApply eq(String field);

    <E> IFormApply ne(IGetter<E> getter);

    IFormApply ne(String field);

    <E> IFormApply gt(IGetter<E> getter);

    IFormApply gt(String field);

    <E> IFormApply ge(IGetter<E> getter);

    IFormApply ge(String field);

    <E> IFormApply lt(IGetter<E> getter);

    IFormApply lt(String field);

    <E> IFormApply le(IGetter<E> getter);

    IFormApply le(String field);

    <E> IFormApply like(IGetter<E> getter);

    IFormApply like(String field);

    <E> IFormApply likeLeft(IGetter<E> getter);

    IFormApply likeLeft(String field);

    <E> IFormApply likeRight(IGetter<E> getter);

    IFormApply likeRight(String field);
}