package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.segment.JoinOn;

@FunctionalInterface
public interface JoinOnConsumer<Q1 extends BaseQuery<?, Q1>, Q2 extends BaseQuery<?, Q2>> {
    void accept(Q1 q1, Q2 q2, JoinOn join);
}
