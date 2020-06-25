package cn.org.atool.fluent.mybatis.functions;

/**
 * 聚合接口
 *
 * @author wudarui
 */
@FunctionalInterface
public interface IAggregate {
    /**
     * 聚合函数表达式
     *
     * @param column 聚合字段
     * @return 聚合表达式
     */
    String aggregate(String column);
}
