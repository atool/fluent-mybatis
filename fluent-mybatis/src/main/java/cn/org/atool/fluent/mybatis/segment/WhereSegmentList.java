package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.segment.model.ISqlSegment;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.SPACE;
import static cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment.AND;

/**
 * 普通片段
 *
 * @author darui.wu
 */
public class WhereSegmentList extends BaseSegmentList {

    @Override
    public WhereSegmentList add(ISqlSegment first, ISqlSegment... segments) {
        if (If.isEmpty(segments)) {
            return this;
        }
        if (first.isAndOr()) {
            if (!this.segments.isEmpty() && !this.lastIsAndOr()) {
                this.segments.add(first);
            }
        } else {
            if (!this.segments.isEmpty() && !this.lastIsAndOr()) {
                this.segments.add(AND);
            }
            this.segments.add(first);
        }
        super.addAll(segments);
        return this;
    }

    /**
     * 最后一个元素是否是AND 或者 OR
     *
     * @return true: AND OR
     */
    private boolean lastIsAndOr() {
        return !this.segments.isEmpty() && this.segments.get(this.segments.size() - 1).isAndOr();
    }

    /**
     * 示例: column1 = ? AND column2 = ?
     *
     * @return sql
     */
    @Override
    public String build() {
        if (this.lastIsAndOr()) {
            this.segments.remove(this.segments.size() - 1);
        }
        return super.merge(EMPTY, SPACE);
    }
}