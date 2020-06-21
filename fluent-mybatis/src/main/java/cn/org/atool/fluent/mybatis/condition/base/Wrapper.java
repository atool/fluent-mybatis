package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.condition.model.*;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.interfaces.IEntity;
import cn.org.atool.fluent.mybatis.interfaces.IQuery;
import cn.org.atool.fluent.mybatis.interfaces.IWrapper;
import cn.org.atool.fluent.mybatis.utility.NestedQueryFactory;
import lombok.Getter;

import java.util.Map;
import java.util.function.Consumer;

import static cn.org.atool.fluent.mybatis.condition.model.Constants.EMPTY;
import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.AND;
import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.OR;
import static cn.org.atool.fluent.mybatis.condition.model.SqlOp.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;

/**
 * 查询条件封装
 *
 * @param <E> 对应的实体类
 * @param <W> 更新器或查询器
 * @param <Q> 对应的嵌套查询器
 * @author darui.wu
 */
public abstract class Wrapper<E extends IEntity, W, Q extends IQuery<E, Q>> implements IWrapper<E, W, Q> {
    private static final long serialVersionUID = 2674302532927710150L;
    /**
     * 实体类型
     */
    @Getter
    protected final Class<E> entityClass;
    /**
     * 自定义参数列表
     */
    @Getter
    protected final ParameterPair parameters;
    /**
     * where, group, having ,order by对象列表
     */
    protected final MergeSegments mergeSegments;
    /**
     * 分页参数
     */
    protected PagedOffset paged;

    protected Wrapper(Class<E> entityClass) {
        this(entityClass, new ParameterPair());
    }

    protected Wrapper(Class<E> entityClass, ParameterPair parameters) {
        notNull(entityClass, "entityClass must not null,please set entity before use this method!");
        this.entityClass = entityClass;
        this.parameters = parameters;
        this.mergeSegments = new MergeSegments();
    }

    /**
     * 返回查询器自身
     *
     * @return self
     */
    private W typeThis() {
        return (W) this;
    }

    @Override
    public <V> W eqByMap(Map<String, V> params, boolean ignoreNull) {
        if (isEmpty(params)) {
            return typeThis();
        }
        params.forEach((k, v) -> {
            if (isNotNull(v)) {
                apply(AND, k, EQ, v);
            } else if (!ignoreNull) {
                apply(AND, k, IS_NULL);
            }
        });
        return typeThis();
    }

    @Override
    public W apply(KeyWordSegment keyWord, String column, String format, SqlOp operator, Object... paras) {
        this.validateColumn(column);
        if (keyWord == null) {
            throw new FluentMybatisException("the first segment should be: 'AND', 'OR', 'GROUP BY', 'HAVING' or 'ORDER BY'");
        }
        String segment = operator.operator(this.parameters, format, paras);
        mergeSegments.add(keyWord, () -> column, () -> segment);
        return typeThis();
    }

    @Override
    public W and(String applySql, Object... paras) {
        mergeSegments.add(AND, () -> this.parameters.paramSql(applySql, paras));
        return typeThis();
    }

    @Override
    public W and(Consumer<Q> query) {
        final Q nested = NestedQueryFactory.nested(this.queryClass(), this.parameters);
        query.accept(nested);
        return this.apply(AND, EMPTY, nested.getWhereSql(), BRACKET);
    }

    @Override
    public W or(String applySql, Object... paras) {
        mergeSegments.add(OR, () -> this.parameters.paramSql(applySql, paras));
        return typeThis();
    }

    @Override
    public W or(Consumer<Q> consumer) {
        final Q nested = NestedQueryFactory.nested(this.queryClass(), this.parameters);
        consumer.accept(nested);
        return this.apply(OR, EMPTY, nested.getWhereSql(), BRACKET);
    }

    @Override
    public W last(String lastSql) {
        this.mergeSegments.setLastSql(lastSql);
        return typeThis();
    }

    /**
     * 返回主键字段
     *
     * @return 主键字段
     */
    protected String primaryName() {
        throw new RuntimeException("primary column undefined");
    }

    /**
     * 判断字段是否在范围内
     *
     * @param column 字段
     * @return 是否合法
     */
    protected boolean validateColumn(String column) {
        return true;
    }

    /**
     * 返回对应嵌套查询定义类
     *
     * @return 嵌套查询定义类
     */
    public abstract Class<Q> queryClass();

    /**
     * where 语句部分
     *
     * @return sql
     */
    public String getWhereSql() {
        return mergeSegments.sql();
    }

    /**
     * 返回entity实体属性和数据库字段的映射关系
     *
     * @return
     */
    protected abstract Map<String, String> property2Column();
}