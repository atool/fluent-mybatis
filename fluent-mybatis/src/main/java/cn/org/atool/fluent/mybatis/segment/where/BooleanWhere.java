package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.segment.WhereBase;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;

/**
 * 布尔值比较
 *
 * @param <WHERE>
 * @param <NQ>
 */
public interface BooleanWhere<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IQuery<?, NQ>
    > extends BaseWhere<WHERE, NQ> {
    /**
     * 等于 true
     *
     * @return 查询器或更新器
     */
    default WHERE isTrue() {
        return this.apply(EQ, Boolean.TRUE);
    }

    /**
     * 等于 false
     *
     * @return 查询器或更新器
     */
    default WHERE isFalse() {
        return this.apply(EQ, Boolean.FALSE);
    }
}
