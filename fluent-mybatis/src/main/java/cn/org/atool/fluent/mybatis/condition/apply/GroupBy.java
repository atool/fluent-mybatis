package cn.org.atool.fluent.mybatis.condition.apply;

import cn.org.atool.fluent.mybatis.condition.base.BaseGroup;

/**
 * GroupBy 设置
 *
 * @author darui.wu
 * @create 2020/6/21 8:08 下午
 */
public class GroupBy<G extends BaseGroup<G>> {
    private G groupBy;

    private String column;

    public GroupBy(G groupBy, String column) {
        this.groupBy = groupBy;
        this.column = column;
    }

    /**
     * 按 column字段分组
     *
     * @return 分组设置器
     */
    public G apply() {
        return groupBy.apply(this.column);
    }
}