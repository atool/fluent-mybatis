package cn.org.atool.fluent.mybatis.segment.list;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.fragment.KeyFrag;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.COMMA_SPACE;
import static cn.org.atool.fluent.mybatis.segment.fragment.KeyFrag.GROUP_BY;

/**
 * Group By SQL 片段
 *
 * @author darui.wu
 */
public class GroupBySegmentList extends BaseSegmentList {

    public GroupBySegmentList() {
        super.segments.setDelimiter(COMMA_SPACE).setFilter(If::notBlank);
    }

    @Override
    public BaseSegmentList add(KeyFrag keyword, IFragment... sqlSegments) {
        super.segments.add(sqlSegments);
        return this;
    }

    /**
     * 示例: group by column1, column2
     *
     * @return sql
     */
    @Override
    public IFragment get() {
        return super.merge(GROUP_BY);
    }
}