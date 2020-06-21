package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.condition.model.PagedOffset;
import cn.org.atool.fluent.mybatis.condition.model.ParameterPair;
import cn.org.atool.fluent.mybatis.interfaces.IEntity;
import cn.org.atool.fluent.mybatis.interfaces.IQuery;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.condition.model.Constants.*;
import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.GROUP_BY;
import static cn.org.atool.fluent.mybatis.condition.model.SqlOp.RETAIN;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;
import static java.util.stream.Collectors.joining;

/**
 * AbstractQueryWrapper
 *
 * @param <E> 对应的实体类
 * @param <Q> 查询器
 * @author darui.wu
 * @date 2020/6/17 3:13 下午
 */
public abstract class BaseQuery<E extends IEntity, Q extends BaseQuery<E, Q>> extends Wrapper<E, Q, Q> implements IQuery<E, Q> {
    private static final long serialVersionUID = 5541270981919538655L;

    private boolean isDistinct = false;
    /**
     * 查询字段
     */
    private Set<String> sqlSelect = new LinkedHashSet<>(8);

    protected BaseQuery(Class entityClass) {
        super(entityClass);
    }

    /**
     * 非对外公开的构造方法,只用于生产嵌套 sql
     */
    protected BaseQuery(Class entityClass, ParameterPair parameters) {
        super(entityClass, parameters);
    }

    @Override
    public Q distinct() {
        this.isDistinct = true;
        return (Q) this;
    }

    @Override
    public Q selectId() {
        return this.select(this.primaryName());
    }

    @Override
    public Q select(String... columns) {
        Stream.of(columns).filter(s -> isNotEmpty(s)).forEach(this.sqlSelect::add);
        return (Q) this;
    }

    @Override
    public Q limit(int limit) {
        super.paged = new PagedOffset(0, limit);
        return (Q) this;
    }

    @Override
    public Q limit(int from, int limit) {
        super.paged = new PagedOffset(from, limit);
        return (Q) this;
    }

    @Override
    public String getQuerySql() {
        String where = this.getWhereSql();
        return String.format("SELECT %s FROM %s WHERE %s", this.getSqlSelect(), this.table(), where);
    }

    /**
     * 查询条件 SQL 片段
     *
     * @return 查询字段列表
     */
    public String getSqlSelect() {
        if (this.sqlSelect.isEmpty()) {
            return ASTERISK;
        } else {
            return (isDistinct ? DISTINCT + SPACE : EMPTY) + sqlSelect.stream().collect(joining(COMMA_SPACE));
        }
    }

    /**
     * 返回表名
     *
     * @return 表名
     */
    protected abstract String table();
}