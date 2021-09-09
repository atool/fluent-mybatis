package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseFormSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.mapper.QueryExecutor;
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

    default S eq() {
        return this.op(OP_EQ);
    }

    IFormApply<E, S> eq(IGetter<E> getter);

    default S ne() {
        return this.op(OP_NE);
    }

    IFormApply<E, S> ne(IGetter<E> getter);

    default S gt() {
        return this.op(OP_GT);
    }

    IFormApply<E, S> gt(IGetter<E> getter);

    default S ge() {
        return this.op(OP_GE);
    }

    IFormApply<E, S> ge(IGetter<E> getter);

    default S lt() {
        return this.op(OP_LT);
    }

    IFormApply<E, S> lt(IGetter<E> getter);

    default S le() {
        return this.op(OP_LE);
    }

    IFormApply<E, S> le(IGetter<E> getter);

    default S like() {
        return this.op(OP_LIKE);
    }

    IFormApply<E, S> like(IGetter<E> getter);

    default S likeLeft() {
        return this.op(OP_LEFT_LIKE);
    }

    IFormApply<E, S> likeLeft(IGetter<E> getter);

    default S likeRight() {
        return this.op(OP_RIGHT_LIKE);
    }

    IFormApply<E, S> likeRight(IGetter<E> getter);

    /**
     * 把表单转换查询对象
     *
     * @return IQuery
     */
    IQuery<E> query();

    /**
     * 把表单直接转换为查询执行对象
     *
     * @return QueryExecutor
     */
    default QueryExecutor<E> to() {
        return this.query().to();
    }
}