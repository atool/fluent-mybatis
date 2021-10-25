package cn.org.atool.fluent.form.setter;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.functions.IGetter;

import static cn.org.atool.fluent.mybatis.base.model.SqlOpStr.*;

/**
 * 简单表单查询
 *
 * @param <S>
 * @author wudarui
 */
@SuppressWarnings({"unused"})
public interface IFormApply<E extends IEntity, S extends BaseFormSetter> {

    S op(String op);

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

    default S eq() {
        return this.op(OP_EQ);
    }

    IFormApply<E, S> eq(IGetter<E> getter);

    IFormApply<E, S> eq(String field);

    default S ne() {
        return this.op(OP_NE);
    }

    IFormApply<E, S> ne(IGetter<E> getter);

    IFormApply<E, S> ne(String field);

    default S gt() {
        return this.op(OP_GT);
    }

    IFormApply<E, S> gt(IGetter<E> getter);

    IFormApply<E, S> gt(String field);

    default S ge() {
        return this.op(OP_GE);
    }

    IFormApply<E, S> ge(IGetter<E> getter);

    IFormApply<E, S> ge(String field);

    default S lt() {
        return this.op(OP_LT);
    }

    IFormApply<E, S> lt(IGetter<E> getter);

    IFormApply<E, S> lt(String field);

    default S le() {
        return this.op(OP_LE);
    }

    IFormApply<E, S> le(IGetter<E> getter);

    IFormApply<E, S> le(String field);

    default S like() {
        return this.op(OP_LIKE);
    }

    IFormApply<E, S> like(IGetter<E> getter);

    IFormApply<E, S> like(String field);

    default S likeLeft() {
        return this.op(OP_LIKE_LEFT);
    }

    IFormApply<E, S> likeLeft(IGetter<E> getter);

    IFormApply<E, S> likeLeft(String field);

    default S likeRight() {
        return this.op(OP_LIKE_RIGHT);
    }

    IFormApply<E, S> likeRight(IGetter<E> getter);

    IFormApply<E, S> likeRight(String field);
}