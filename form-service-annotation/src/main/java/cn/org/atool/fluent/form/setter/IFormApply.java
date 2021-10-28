package cn.org.atool.fluent.form.setter;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.functions.IGetter;

/**
 * 简单表单查询
 *
 * @param <S>
 * @author wudarui
 */
@SuppressWarnings({"unused"})
public interface IFormApply<E extends IEntity, S extends BaseFormSetter> {

    /**
     * 设置更新字段
     *
     * @param getter 字段
     * @return ignore
     */
    IFormApply<E, S> and(IGetter<E> getter);

    /**
     * 设置更新字段
     *
     * @param field 字段
     * @return ignore
     */
    IFormApply<E, S> and(String field);

    IFormApply<E, S> eq(IGetter<E> getter);

    IFormApply<E, S> eq(String field);

    IFormApply<E, S> ne(IGetter<E> getter);

    IFormApply<E, S> ne(String field);

    IFormApply<E, S> gt(IGetter<E> getter);

    IFormApply<E, S> gt(String field);

    IFormApply<E, S> ge(IGetter<E> getter);

    IFormApply<E, S> ge(String field);

    IFormApply<E, S> lt(IGetter<E> getter);

    IFormApply<E, S> lt(String field);

    IFormApply<E, S> le(IGetter<E> getter);

    IFormApply<E, S> le(String field);

    IFormApply<E, S> like(IGetter<E> getter);

    IFormApply<E, S> like(String field);

    IFormApply<E, S> likeLeft(IGetter<E> getter);

    IFormApply<E, S> likeLeft(String field);

    IFormApply<E, S> likeRight(IGetter<E> getter);

    IFormApply<E, S> likeRight(String field);
}