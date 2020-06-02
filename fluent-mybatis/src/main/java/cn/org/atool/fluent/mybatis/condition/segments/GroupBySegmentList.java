package cn.org.atool.fluent.mybatis.condition.segments;

import cn.org.atool.fluent.mybatis.condition.interfaces.ISqlSegment;

import java.util.List;

import static cn.org.atool.fluent.mybatis.util.Constants.*;
import static cn.org.atool.fluent.mybatis.condition.helper.SqlKeyword.GROUP_BY;
import static java.util.stream.Collectors.joining;

/**
 * Group By SQL 片段
 *
 * @author darui.wu
 */
public class GroupBySegmentList extends AbstractISegmentList {

    @Override
    protected boolean transformList(List<ISqlSegment> list, ISqlSegment firstSegment) {
        list.remove(0);
        return true;
    }

    @Override
    protected String childrenSqlSegment() {
        if (isEmpty()) {
            return EMPTY;
        }
        return this.stream().map(ISqlSegment::getSqlSegment).collect(joining(COMMA, SPACE + GROUP_BY.getSqlSegment() + SPACE, EMPTY));
    }
}