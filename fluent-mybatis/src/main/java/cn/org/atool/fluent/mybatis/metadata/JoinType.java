package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;

import static cn.org.atool.fluent.mybatis.utility.StrConstant.SPACE;

/**
 * 关联查询类型
 */
public enum JoinType implements IFragment {
    /**
     * inner join
     */
    Join("JOIN"),
    /**
     * left join
     */
    LeftJoin("LEFT JOIN"),
    /**
     * right join
     */
    RightJoin("RIGHT JOIN");

    private final String join;

    JoinType(String join) {
        this.join = join;
    }

    @Override
    public String get(IMapping mapping) {
        return this.join + SPACE;
    }
}