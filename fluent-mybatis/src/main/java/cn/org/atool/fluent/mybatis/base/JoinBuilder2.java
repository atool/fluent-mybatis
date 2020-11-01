package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.segment.JoinOn;
import cn.org.atool.fluent.mybatis.segment.JoinQuery;

import java.util.function.Function;

/**
 * 通过lambda表达方式构造join条件
 * 但IDE对lambda表达式解析不是很完善，暂时隐蔽
 * <p>
 * 请使用JoinBuilder方式构造join条件
 *
 * @param <QL>
 */
public interface JoinBuilder2<QL extends BaseQuery<?, QL>> {
    /**
     * 构建Join Query Builder对象
     *
     * @param clazz
     * @param query
     * @param <QL>
     * @return
     */
    static <QL extends BaseQuery<?, QL>> JoinBuilder2<QL> from(Class<QL> clazz, Function<QL, QL> query) {
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
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder2<QL>> join(Class<QR> clazz, Function<QR, QR> query);

    /**
     * from left.table left join right.table on condition
     *
     * @param clazz join right 表类型
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right 表类型
     * @return
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder2<QL>> leftJoin(Class<QR> clazz, Function<QR, QR> query);

    /**
     * from left.table right join right.table on condition
     *
     * @param clazz join right 表类型
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right 表类型
     * @return
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder2<QL>> rightJoin(Class<QR> clazz, Function<QR, QR> query);

    /**
     * distinct
     *
     * @return
     */
    JoinBuilder2<QL> distinct();

    /**
     * limit 0, limit
     *
     * @param limit
     * @return
     */
    JoinBuilder2<QL> limit(int limit);

    /**
     * limit start, limit
     *
     * @param start
     * @param limit
     * @return
     */
    JoinBuilder2<QL> limit(int start, int limit);

    /**
     * 追加在sql语句的末尾
     * !!!慎用!!!
     * 有sql注入风险
     *
     * @param lastSql
     * @return
     */
    JoinBuilder2<QL> last(String lastSql);

    IQuery<?, QL> build();
}
