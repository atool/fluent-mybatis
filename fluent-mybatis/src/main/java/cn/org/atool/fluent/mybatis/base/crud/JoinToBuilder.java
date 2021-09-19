package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.segment.JoinOn;

/**
 * 关联查询构造方式一: 使用直接传入设置好别名和参数的Query
 *
 * @param <QL> 查询表一
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "unused"})
public interface JoinToBuilder<QL extends BaseQuery<?, QL>> extends JoinBuilder<QL> {
    /**
     * from left.table join right.table on condition
     *
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right表类型
     * @return ignore
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinToBuilder<QL>> join(QR query);

    /**
     * from table1 join (select query) alias ...
     *
     * @param query 子查询
     * @param alias 子查询别名
     * @param <QR>  右查询类型
     * @return ignore
     */
    default <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinToBuilder<QL>> join(QR query, String alias) {
        return this.join((QR) new FreeQuery(query, alias));
    }

    /**
     * from left.table left join right.table on condition
     *
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right 表类型
     * @return ignore
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinToBuilder<QL>> leftJoin(QR query);

    /**
     * from table1 left join (select query) alias ...
     *
     * @param query 子查询
     * @param alias 子查询别名
     * @param <QR>  右查询类型
     * @return ignore
     */
    default <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinToBuilder<QL>> leftJoin(QR query, String alias) {
        return this.leftJoin((QR) new FreeQuery(query, alias));
    }

    /**
     * from left.table right join right.table on condition
     *
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right 表类型
     * @return ignore
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinToBuilder<QL>> rightJoin(QR query);

    /**
     * from table1 right join (select query) alias ...
     *
     * @param query 子查询
     * @param alias 子查询别名
     * @param <QR>  右查询类型
     * @return ignore
     */
    default <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinToBuilder<QL>> rightJoin(QR query, String alias) {
        return this.rightJoin((QR) new FreeQuery(query, alias));
    }
}