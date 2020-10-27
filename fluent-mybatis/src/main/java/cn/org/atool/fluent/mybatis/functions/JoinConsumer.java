package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.JoinOn;
import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;

@FunctionalInterface
public interface JoinConsumer<Q1 extends BaseQuery<?, Q1>, Q2 extends BaseQuery<?, Q2>> {
    void accept(JoinOn join, Q1 q1, Q2 q2);
}