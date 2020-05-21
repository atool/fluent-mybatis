package cn.org.atool.fluent.mybatis.condition.segments;

import cn.org.atool.fluent.mybatis.condition.interfaces.ISqlSegment;
import cn.org.atool.fluent.mybatis.condition.SqlKeyword;
import cn.org.atool.fluent.mybatis.condition.WrapperKeyword;

import java.util.function.Predicate;

/**
 * 匹配片段
 *
 * @author darui.wu
 */
public enum MatchSegment {
    /**
     * group by
     */
    GROUP_BY(i -> i == SqlKeyword.GROUP_BY),
    /**
     * order by
     */
    ORDER_BY(i -> i == SqlKeyword.ORDER_BY),
    /**
     * not
     */
    NOT(i -> i == SqlKeyword.NOT),
    /**
     * and
     */
    AND(i -> i == SqlKeyword.AND),
    /**
     * or
     */
    OR(i -> i == SqlKeyword.OR),
    /**
     * and or or
     */
    AND_OR(i -> i == SqlKeyword.AND || i == SqlKeyword.OR),
    /**
     * exists
     */
    EXISTS(i -> i == SqlKeyword.EXISTS),
    /**
     * having
     */
    HAVING(i -> i == SqlKeyword.HAVING),
    /**
     * apply
     */
    APPLY(i -> i == WrapperKeyword.APPLY);

    private final Predicate<ISqlSegment> predicate;

    MatchSegment(Predicate<ISqlSegment> predicate) {
        this.predicate = predicate;
    }

    public boolean match(ISqlSegment segment) {
        return getPredicate().test(segment);
    }

    protected Predicate<ISqlSegment> getPredicate() {
        return predicate;
    }
}