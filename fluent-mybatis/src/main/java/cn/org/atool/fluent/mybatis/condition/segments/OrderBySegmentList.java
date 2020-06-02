package cn.org.atool.fluent.mybatis.condition.segments;

import cn.org.atool.fluent.mybatis.condition.interfaces.ISqlSegment;

import java.util.List;

import static cn.org.atool.fluent.mybatis.util.Constants.*;
import static cn.org.atool.fluent.mybatis.condition.helper.SqlKeyword.ORDER_BY;
import static java.util.stream.Collectors.joining;

/**
 * Order By SQL 片段
 *
 * @author darui.wu
 */
@SuppressWarnings("serial")
public class OrderBySegmentList extends AbstractISegmentList {

    @Override
    protected boolean transformList(List<ISqlSegment> list, ISqlSegment firstSegment) {
        list.remove(0);
        if (!isEmpty()) {
            super.add(() -> COMMA);
        }
        return true;
    }

    @Override
    protected String childrenSqlSegment() {
        if (isEmpty()) {
            return EMPTY;
        }
        return this.stream().map(ISqlSegment::getSqlSegment).collect(joining(SPACE, SPACE + ORDER_BY.getSqlSegment() + SPACE, EMPTY));
    }
}