package cn.org.atool.fluent.mybatis.segment.list;

import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.fragment.KeyFrag;

import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.SEG_EMPTY;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.SEG_SPACE;
import static cn.org.atool.fluent.mybatis.segment.fragment.KeyFrag.AND;
import static cn.org.atool.fluent.mybatis.segment.fragment.KeyFrag.HAVING;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.SPACE;

/**
 * Having SQL 片段
 *
 * @author darui.wu
 */
public class HavingSegmentList extends BaseSegmentList {
    public HavingSegmentList() {
        super.segments.setDelimiter(SPACE).setFilter(NOT_ONLY_KEY);
    }

    @Override
    public BaseSegmentList add(KeyFrag keyword, IFragment... sqlSegments) {
        IFragment merged = this.segments.isEmpty() ? SEG_EMPTY : AND;
        boolean needSpace = false;
        for (IFragment seg : sqlSegments) {
            if (needSpace) {
                merged = merged.plus(SEG_SPACE);
            } else {
                needSpace = true;
            }
            merged = merged.plus(seg);
        }
        super.segments.add(merged);
        return this;
    }

    /**
     * 示例: having sum(column1) > 0 and avg(column2) = 9
     *
     * @return sql
     */
    @Override
    public IFragment get() {
        return super.merge(HAVING);
    }
}