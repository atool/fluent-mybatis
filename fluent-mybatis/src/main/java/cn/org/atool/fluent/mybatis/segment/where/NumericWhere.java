package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.segment.WhereBase;

import java.util.function.Predicate;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.IN;
import static cn.org.atool.fluent.mybatis.base.model.SqlOp.NOT_IN;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.toArray;

/**
 * 数字相关的比较
 *
 * @param <WHERE>
 * @param <NQ>
 */
public interface NumericWhere<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IBaseQuery<?, NQ>
    > extends ObjectWhere<WHERE, NQ> {
    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE in(int[] values) {
        return this.apply(IN, toArray(values));
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @param when   条件成立时
     * @return 查询器或更新器
     */
    default WHERE in(int[] values, Predicate<int[]> when) {
        return this.apply(args -> when.test(values), IN, toArray(values));
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE in(long[] values) {
        return this.apply(IN, toArray(values));
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @param when   条件成立时
     * @return 查询器或更新器
     */
    default WHERE in(long[] values, Predicate<long[]> when) {
        return this.apply(args -> when.test(values), IN, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn(int[] values) {
        return this.apply(NOT_IN, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @param when   条件成立时
     * @return 查询器或更新器
     */
    default WHERE notIn(int[] values, Predicate<int[]> when) {
        return this.apply(args -> when.test(values), NOT_IN, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn(long[] values) {
        return this.apply(NOT_IN, toArray(values));
    }


    /**
     * not in (values)
     *
     * @param values 条件值
     * @param when   条件成立时
     * @return 查询器或更新器
     */
    default WHERE notIn(long[] values, Predicate<long[]> when) {
        return this.apply(args -> when.test(values), NOT_IN, toArray(values));
    }
}