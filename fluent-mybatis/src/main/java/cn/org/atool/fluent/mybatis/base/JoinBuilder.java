package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.segment.JoinOn;
import cn.org.atool.fluent.mybatis.segment.JoinQuery;

import java.util.function.Function;

/**
 * 关联查询接口
 *
 * @param <QL> 查询表一
 * @author wudarui
 */
public interface JoinBuilder<QL extends BaseQuery<?, QL>> {
    /**
     * 构建Join Query Builder对象
     *
     * @param clazz
     * @param query
     * @param <QL>
     * @return
     */
    static <QL extends BaseQuery<?, QL>> JoinBuilder<QL> from(Class<QL> clazz, Function<QL, QL> query) {
        return new JoinQuery<>(clazz, query);
    }

    /**
     * from left.table join right.table on condition
     *
     * @param clazz join right表类型
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right表类型
     * @return
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR> join(Class<QR> clazz, Function<QR, QR> query);

    /**
     * from left.table left join right.table on condition
     *
     * @param clazz join right 表类型
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right 表类型
     * @return
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR> leftJoin(Class<QR> clazz, Function<QR, QR> query);

    /**
     * from left.table right join right.table on condition
     *
     * @param clazz join right 表类型
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right 表类型
     * @return
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR> rightJoin(Class<QR> clazz, Function<QR, QR> query);

    /**
     * distinct
     *
     * @return
     */
    JoinBuilder<QL> distinct();

    /**
     * limit 0, limit
     *
     * @param limit
     * @return
     */
    JoinBuilder<QL> limit(int limit);

    /**
     * limit start, limit
     *
     * @param start
     * @param limit
     * @return
     */
    JoinBuilder<QL> limit(int start, int limit);

    /**
     * 追加在sql语句的末尾
     * !!!慎用!!!
     * 有sql注入风险
     *
     * @param lastSql
     * @return
     */
    JoinBuilder<QL> last(String lastSql);

    default IQuery<?, QL> build() {
        return (IQuery<?, QL>) this;
    }
}