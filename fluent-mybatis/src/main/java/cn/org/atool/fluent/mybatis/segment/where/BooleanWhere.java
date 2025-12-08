package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.segment.WhereBase;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;

/**
 * 布尔值比较
 *
 * @param <WHERE> 条件设置器类型
 * @param <NQ>    对应的查询器类型
 */
@SuppressWarnings({ "unused" })
public interface BooleanWhere<WHERE extends WhereBase<WHERE, ?, NQ>, NQ extends IBaseQuery<?, NQ>>
        extends BaseWhere<WHERE, NQ> {
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