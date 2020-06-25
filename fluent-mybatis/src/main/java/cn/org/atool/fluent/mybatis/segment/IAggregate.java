package cn.org.atool.fluent.mybatis.segment;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * 聚合函数枚举
 *
 * @author wudarui
 */
public enum IAggregate {
    /**
     * 求总和
     */
    SUM("SUM(%s)"),
    /**
     * 求总数
     */
    COUNT("COUNT(%s)"),
    /**
     * 求最大值
     */
    MAX("MAX(%s)"),
    /**
     * 求最小值
     */
    MIN("MIN(%s)"),
    /**
     * 求平均值
     */
    AVG("AVG(%s)"),
    /**
     * 组内连接
     */
    GROUP_CONCAT("GROUP_CONCAT(%s)");

    private String expression;

    IAggregate(String expression) {
        this.expression = expression;
    }

    public String expression(String column) {
        return String.format(expression, column);
    }

    public String expression(String column, String alias) {
        if (isNotEmpty(alias)) {
            return expression(column) + " AS " + alias;
        } else {
            return expression(column);
        }
    }
}
