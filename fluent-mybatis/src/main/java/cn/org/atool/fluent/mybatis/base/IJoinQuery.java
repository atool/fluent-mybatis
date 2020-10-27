package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.functions.JoinConsumer;
import cn.org.atool.fluent.mybatis.segment.JoinQuery;

import java.util.function.Function;

/**
 * 关联查询接口
 *
 * @param <Q1> 查询表一
 * @author wudarui
 */
public interface IJoinQuery<Q1 extends BaseQuery<?, Q1>> {
    /**
     * from query1.table join query2.table on condition
     *
     * @param clazz
     * @param query 关联查询右表及右表条件设置
     * @param join  关联关系设置
     * @return
     */
    <Q2 extends BaseQuery<?, Q2>> IJoinQuery<Q1> join(Class<Q2> clazz, Function<Q2, Q2> query, JoinConsumer<Q1, Q2> join);

    /**
     * from query1.table left join query2.table on condition
     *
     * @param query 关联查询右表及右表条件设置
     * @param join  关联关系设置
     * @return
     */
    <Q2 extends BaseQuery<?, Q2>> IJoinQuery<Q1> leftJoin(Class<Q2> clazz, Function<Q2, Q2> query, JoinConsumer<Q1, Q2> join);

    /**
     * from query1.table right join query2.table on condition
     *
     * @param query 关联查询右表及右表条件设置
     * @param join  关联关系设置
     * @return
     */
    <Q2 extends BaseQuery<?, Q2>> IJoinQuery<Q1> rightJoin(Class<Q2> clazz, Function<Q2, Q2> query, JoinConsumer<Q1, Q2> join);

    /**
     * distinct
     *
     * @return
     */
    IJoinQuery<Q1> distinct();

    /**
     * limit 0, limit
     *
     * @param limit
     * @return
     */
    IJoinQuery<Q1> limit(int limit);

    /**
     * limit start, limit
     *
     * @param start
     * @param limit
     * @return
     */
    IJoinQuery<Q1> limit(int start, int limit);

    /**
     * 追加在sql语句的末尾
     * !!!慎用!!!
     * 有sql注入风险
     *
     * @param lastSql
     * @return
     */
    IJoinQuery<Q1> last(String lastSql);

    default JoinQuery<Q1> build() {
        return (JoinQuery<Q1>) this;
    }
}