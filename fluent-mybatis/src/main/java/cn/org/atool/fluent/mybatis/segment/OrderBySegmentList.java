package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.segment.model.ISqlSegment;

import static cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment.ORDER_BY;
import static cn.org.atool.fluent.mybatis.segment.model.StrConstant.COMMA_SPACE;

/**
 * Order By SQL 片段
 *
 * @author darui.wu
 */
public class OrderBySegmentList extends BaseSegmentList {

    @Override
    public BaseSegmentList add(ISqlSegment first, ISqlSegment... sqlSegments) {
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