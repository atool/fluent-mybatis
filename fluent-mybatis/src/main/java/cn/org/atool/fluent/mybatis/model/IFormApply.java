package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseFormSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.mapper.QueryExecutor;

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

    default S ne() {
        return this.op(OP_NE);
    }

    default S gt() {
        return this.op(OP_GT);
    }

    default S ge() {
        return this.op(OP_GE);
    }

    default S lt() {
        return this.op(OP_LT);
    }

    default S le() {
        return this.op(OP_LE);
    }

    default S like() {
        return this.op(OP_LIKE);
    }

    default S leftLike() {
        return this.op(OP_LEFT_LIKE);
    }

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