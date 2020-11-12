package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.FormSetter;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IQuery;

import static cn.org.atool.fluent.mybatis.model.FormItemOp.OP_EQ;

public interface IFormQuery<E extends IEntity, C extends FormSetter<IFormQuery<E, C>>> extends IQuery<E, IFormQuery<E, C>> {

    C op(String op);

    default C eq() {
        return this.op(OP_EQ);
    }

    default C ne() {
        return this.op(FormItemOp.OP_NE);
    }

    default C gt() {
        return this.op(FormItemOp.OP_GT);
    }

    default C ge() {
        return this.op(FormItemOp.OP_GE);
    }

    default C lt() {
        return this.op(FormItemOp.OP_LT);
    }

    default C le() {
        return this.op(FormItemOp.OP_LE);
    }

    default C like() {
        return this.op(FormItemOp.OP_LIKE);
    }
}