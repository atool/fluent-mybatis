package cn.org.atool.fluent.mybatis.condition.segments;

import cn.org.atool.fluent.mybatis.condition.interfaces.ISqlSegment;
import cn.org.atool.fluent.mybatis.condition.SqlKeyword;

import java.util.List;

import static cn.org.atool.fluent.mybatis.util.Constants.EMPTY;
import static cn.org.atool.fluent.mybatis.util.Constants.SPACE;
import static cn.org.atool.fluent.mybatis.condition.SqlKeyword.HAVING;
import static java.util.stream.Collectors.joining;

/**
 * Having SQL 片段
 *
 * @author darui.wu
 */
@SuppressWarnings("serial")
public class HavingSegmentList extends AbstractISegmentList {

    @Override
    protected boolean transformList(List<ISqlSegment> list, ISqlSegment firstSegment) {
        if (!isEmpty()) {
            this.add(SqlKeyword.AND);
        }
        list.remove(0);
        return true;
    }

    @Override
    protected String childrenSqlSegment() {
        if (isEmpty()) {
            return EMPTY;
        }
        return this.stream().map(ISqlSegment::getSqlSegment).collect(joining(SPACE, SPACE + HAVING.getSqlSegment() + SPACE, EMPTY));
    }
}