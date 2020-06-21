package cn.org.atool.fluent.mybatis.condition.model.segments;

import cn.org.atool.fluent.mybatis.interfaces.ISqlSegment;

import static cn.org.atool.fluent.mybatis.condition.model.Constants.COMMA_SPACE;
import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.GROUP_BY;

/**
 * Group By SQL 片段
 *
 * @author darui.wu
 */
public class GroupBySegmentList extends AbstractSegmentList {

    @Override
    public AbstractSegmentList add(ISqlSegment first, ISqlSegment... sqlSegments) {
        return super.addAll(sqlSegments);
    }

    /**
     * 示例: group by column1, column2
     *
     * @return sql
     */
    @Override
    public String build() {
        return super.merge(GROUP_BY.getSqlSegment(), COMMA_SPACE);
    }
}