package cn.org.atool.fluent.mybatis.segment.model;

public enum Aggregate {
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
    AVG("AVG(%s)");

    private String expression;

    Aggregate(String expression) {
        this.expression = expression;
    }

    public String expression(Object... args) {
        return String.format(expression, args);
    }
}
