package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.base.IQuery;

/**
 * HavingBy 设置
 *
 * @param <H>
 * @param <Q>
 * @author darui.wu
 * @create 2020/6/21 6:18 下午
 */
public class HavingApply<
    H extends HavingBase<H, Q>,
    Q extends IQuery<?, Q>
    > extends BaseApply<H, Q> {

    public HavingApply(H having) {
        super(having);
    }

    /**
     * 字段之和 符合 分组条件
     *
     * @param op   比较操作
     * @param args 比较参数
     * @return 返回字段选择器
     */
    public H sum(SqlOp op, Object... args) {
        return this.segment.apply("SUM(" + this.current.column + ")", op, args);
    }

    /**
     * 非空字段总数 符合 分组条件
     *
     * @param op   比较操作
     * @param args 比较参数
     * @return 返回字段选择器
     */
    public H count(SqlOp op, Object... args) {
        return this.segment.apply("COUNT(" + this.current.column + ")", op, args);
    }

    /**
     * 字段最大值 符合 分组条件
     *
     * @param op   比较操作
     * @param args 比较参数
     * @return 返回字段选择器
     */
    public H max(SqlOp op, Object... args) {
        return this.segment.apply("MAX(" + this.current.column + ")", op, args);
    }

    /**
     * 字段最小值 符合 分组条件
     *
     * @param op   比较操作
     * @param args 比较参数
     * @return 返回字段选择器
     */
    public H min(SqlOp op, Object... args) {
        return this.segment.apply("MIN(" + this.current.column + ")", op, args);
    }

    /**
     * 字段平均值 符合 分组条件
     *
     * @param op   比较操作
     * @param args 比较参数
     * @return 返回字段选择器
     */
    public H avg(SqlOp op, Object... args) {
        return this.segment.apply("AVG(" + this.current.column + ")", op, args);
    }
}