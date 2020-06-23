package cn.org.atool.fluent.mybatis.condition;

import cn.org.atool.fluent.mybatis.interfaces.ISqlSegment;

import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.AND;
import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.HAVING;
import static cn.org.atool.fluent.mybatis.condition.model.StrConstant.SPACE;

/**
 * Having SQL 片段
 *
 * @author darui.wu
 */
public class HavingSegmentList extends BaseSegmentList {

    @Override
    public BaseSegmentList add(ISqlSegment first, ISqlSegment... sqlSegments) {
        if (!this.segments.isEmpty()) {
            this.segments.add(AND);
        }
        return super.addAll(sqlSegments);
    }

    /**
     * 示例: having sum(column1) > 0 and avg(column2) = 9
     *
     * @return sql
     */
    @Override
    public String build() {
        return super.merge(HAVING.getSqlSegment(), SPACE);
    }
}