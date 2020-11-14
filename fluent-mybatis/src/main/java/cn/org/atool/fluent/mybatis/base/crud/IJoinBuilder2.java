package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.functions.QFunction;
import cn.org.atool.fluent.mybatis.segment.JoinOn;

/**
 * 关联查询构造方式二: 使用lambda表达式,由框架自动设置query别名和关联参数
 * <p>
 * 注: 在有些场景下, IDE对lambda表达式的代码提示不够智能
 * <p>
 *
 * @param <QL>
 */
public interface IJoinBuilder2<QL extends BaseQuery<?, QL>> extends JoinBuilder<QL> {
    /**
     * from left.table join right.table on condition
     *
     * @param clazz join right表类型
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right表类型
     * @return
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, IJoinBuilder2<QL>> join(Class<QR> clazz, QFunction<QR> query);

    /**
     * from left.table left join right.table on condition
     *
     * @param clazz join right 表类型
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right 表类型
     * @return
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, IJoinBuilder2<QL>> leftJoin(Class<QR> clazz, QFunction<QR> query);

    /**
     * from left.table right join right.table on condition
     *
     * @param clazz join right 表类型
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right 表类型
     * @return
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, IJoinBuilder2<QL>> rightJoin(Class<QR> clazz, QFunction<QR> query);

    /**
     * distinct
     *
     * @return
     */
    IJoinBuilder2<QL> distinct();

    /**
     * limit 0, limit
     *
     * @param limit
     * @return
     */
    IJoinBuilder2<QL> limit(int limit);

    /**
     * limit start, limit
     *
     * @param start
     * @param limit
     * @return
     */
    IJoinBuilder2<QL> limit(int start, int limit);

    /**
     * 追加在sql语句的末尾
     * !!!慎用!!!
     * 有sql注入风险
     *
     * @param lastSql
     * @return
     */
    IJoinBuilder2<QL> last(String lastSql);
}
