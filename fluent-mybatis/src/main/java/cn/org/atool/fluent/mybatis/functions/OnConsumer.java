package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.segment.JoinOnBuilder;

public interface OnConsumer<QL extends BaseQuery<?, QL>, QR extends BaseQuery<?, QR>> {
    void accept(JoinOnBuilder join, QL l, QR r);
}