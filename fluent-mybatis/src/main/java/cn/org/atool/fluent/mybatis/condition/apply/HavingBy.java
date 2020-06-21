package cn.org.atool.fluent.mybatis.condition.apply;

import cn.org.atool.fluent.mybatis.condition.base.BaseHaving;
import cn.org.atool.fluent.mybatis.condition.model.SqlOp;

/**
 * HavingBy 设置
 *
 * @author darui.wu
 * @create 2020/6/21 6:18 下午
 */
public class HavingBy<H extends BaseHaving<H>> {
    private H having;

    private String column;

    public HavingBy(H having, String column) {
        this.having = having;
        this.column = column;
    }

    /**
     * 字段之和 符合 分组条件
     *
     * @param op   比较操作
     * @param args 比较参数
     * @return 返回字段选择器
     */
    public H sum(SqlOp op, Object... args) {
        return this.having.apply("SUM(" + column + ")", op, args);
    }

    /**
     * 非空字段总数 符合 分组条件
     *
     * @param op   比较操作
     * @param args 比较参数
     * @return 返回字段选择器
     */
    public H count(SqlOp op, Object... args) {
        return this.having.apply("COUNT(" + column + ")", op, args);
    }

    /**
     * 字段最大值 符合 分组条件
     *
     * @param op   比较操作
     * @param args 比较参数
     * @return 返回字段选择器
     */
    public H max(SqlOp op, Object... args) {
        return this.having.apply("MAX(" + column + ")", op, args);
    }

    /**
     * 字段最小值 符合 分组条件
     *
     * @param op   比较操作
     * @param args 比较参数
     * @return 返回字段选择器
     */
    public H min(SqlOp op, Object... args) {
        return this.having.apply("MIN(" + column + ")", op, args);
    }

    /**
     * 字段平均值 符合 分组条件
     *
     * @param op   比较操作
     * @param args 比较参数
     * @return 返回字段选择器
     */
    public H avg(SqlOp op, Object... args) {
        return this.having.apply("AVG(" + column + ")", op, args);
    }
}