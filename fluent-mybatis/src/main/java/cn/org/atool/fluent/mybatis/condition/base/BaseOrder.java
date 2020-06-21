package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.interfaces.IQuery;

import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.condition.model.Constants.*;
import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.ORDER_BY;
import static cn.org.atool.fluent.mybatis.condition.model.SqlOp.RETAIN;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;
import static java.util.stream.Collectors.joining;

/**
 * BaseOrder: 排序对象基类
 *
 * @param <O> 排序对象
 * @author darui.wu
 */
public abstract class BaseOrder<O extends BaseOrder> {
    private final IQuery query;

    protected BaseOrder(IQuery query) {
        this.query = query;
    }

    /**
     * 按升序排：ORDER BY 字段, ... ASC
     * <p>例: asc("id", "name")</p>
     *
     * @param columns 排序字段列表
     * @return 排序对象
     */
    public O asc(String... columns) {
        if (isNotEmpty(columns)) {
            String sql = Stream.of(columns).map(column -> column + SPACE + ASC).collect(joining(COMMA_SPACE));
            this.query.apply(ORDER_BY, EMPTY, sql, RETAIN);
        }
        return (O) this;
    }

    /**
     * 按降序排：ORDER BY 字段, ... DESC
     * <p>例: desc("id", "name")</p>
     *
     * @param columns 排序字段列表
     * @return 排序对象
     */
    public O desc(String... columns) {
        if (isNotEmpty(columns)) {
            String sql = Stream.of(columns).map(column -> column + SPACE + DESC).collect(joining(COMMA_SPACE));
            this.query.apply(ORDER_BY, EMPTY, sql, RETAIN);
        }
        return (O) this;
    }
}