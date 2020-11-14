package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.segment.JoinOn;

/**
 * 关联查询构造方式一: 使用直接传入设置好别名和参数的Query
 *
 * @param <QL> 查询表一
 * @author wudarui
 */
public interface IJoinBuilder1<QL extends BaseQuery<?, QL>>  extends JoinBuilder<QL>{
    /**
     * from left.table join right.table on condition
     *
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right表类型
     * @return
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, IJoinBuilder1<QL>> join(QR query);

    /**
     * from left.table left join right.table on condition
     *
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right 表类型
     * @return
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, IJoinBuilder1<QL>> leftJoin(QR query);

    /**
     * from left.table right join right.table on condition
     *
     * @param query 关联查询右表及右表条件设置
     * @param <QR>  join right 表类型
     * @return
     */
    <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, IJoinBuilder1<QL>> rightJoin(QR query);

    /**
     * distinct
     *
     * @return
     */
    IJoinBuilder1<QL> distinct();

    /**
     * limit 0, limit
     *
     * @param limit
     * @return
     */
    IJoinBuilder1<QL> limit(int limit);

    /**
     * limit start, limit
     *
     * @param start
     * @param limit
     * @return
     */
    IJoinBuilder1<QL> limit(int start, int limit);

    /**
     * 追加在sql语句的末尾
     * !!!慎用!!!
     * 有sql注入风险
     *
     * @param lastSql
     * @return
     */
    IJoinBuilder1<QL> last(String lastSql);
}