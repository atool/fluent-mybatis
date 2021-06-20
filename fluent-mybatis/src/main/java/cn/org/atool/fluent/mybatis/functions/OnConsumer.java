package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.segment.JoinOnBuilder;

/**
 * join on 条件构造
 *
 * @param <QL> 左查询
 * @param <QR> 右查询
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
public interface OnConsumer<QL extends BaseQuery<?, QL>, QR extends BaseQuery<?, QR>> {
    void accept(JoinOnBuilder join, QL l, QR r);
}