package cn.org.atool.fluent.mybatis.condition.model.segments;

import cn.org.atool.fluent.mybatis.interfaces.ISqlSegment;

import static cn.org.atool.fluent.mybatis.condition.model.Constants.COMMA_SPACE;
import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.ORDER_BY;

/**
 * Order By SQL 片段
 *
 * @author darui.wu
 */
public class OrderBySegmentList extends AbstractSegmentList {

    @Override
    public AbstractSegmentList add(ISqlSegment first, ISqlSegment... sqlSegments) {
        return super.addAll(sqlSegments);
    }

    /**
     * 示例: order by column1 asc, column2 desc
     *
     * @return sql
     */
    @Override
    public String build() {
        return super.merge(ORDER_BY.getSqlSegment(), COMMA_SPACE);
    }
}