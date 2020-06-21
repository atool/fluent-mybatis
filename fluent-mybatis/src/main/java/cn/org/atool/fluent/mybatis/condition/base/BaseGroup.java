package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.interfaces.IQuery;

import static cn.org.atool.fluent.mybatis.condition.model.Constants.COMMA_SPACE;
import static cn.org.atool.fluent.mybatis.condition.model.Constants.EMPTY;
import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.GROUP_BY;
import static cn.org.atool.fluent.mybatis.condition.model.SqlOp.RETAIN;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * BaseGroupBy
 *
 * @author darui.wu
 * @create 2020/6/21 8:09 下午
 */
public abstract class BaseGroup<G extends BaseGroup<G>> {

    private IQuery query;

    protected BaseGroup(IQuery query) {
        this.query = query;
    }

    /**
     * 添加group by字段列表
     *
     * @param columns 字段列表
     * @return groupBy选择器
     */
    public G apply(String... columns) {
        if (isNotEmpty(columns)) {
            this.query.apply(GROUP_BY, EMPTY, String.join(COMMA_SPACE, columns), RETAIN);
        }
        return (G) this;
    }
}