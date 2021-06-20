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
public interface JoinBuilder2<QL extends BaseQuery<?, QL>> extends JoinBuilder<QL> {
    /**
     * from left.table join right.table on condition
     *
     * @param clazz join right表类型
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right表类型
     * @return ignore
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder2<QL>> join(Class<QR> clazz, QFunction<QR> query);

    /**
     * from left.table left join right.table on condition
     *
     * @param clazz join right 表类型
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right 表类型
     * @return ignore
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder2<QL>> leftJoin(Class<QR> clazz, QFunction<QR> query);

    /**
     * from left.table right join right.table on condition
     *
     * @param clazz join right 表类型
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right 表类型
     * @return ignore
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder2<QL>> rightJoin(Class<QR> clazz, QFunction<QR> query);
}
