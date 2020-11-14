package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.entity.IEntity;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;

import java.util.List;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.base.model.FieldMapping.alias;

/**
 * AbstractQueryWrapper
 *
 * @param <E> 对应的实体类
 * @param <Q> 查询器
 * @author darui.wu
 * @date 2020/6/17 3:13 下午
 */
public abstract class BaseQuery<
    E extends IEntity,
    Q extends BaseQuery<E, Q>
    >
    extends BaseWrapper<E, Q, Q>
    implements IQuery<E, Q> {

    protected BaseQuery(String table, Class entityClass, Class queryClass) {
        super(table, entityClass, queryClass);
    }

    /**
     * 非对外公开的构造方法,只用于生产嵌套 sql
     */
    protected BaseQuery(String table, Parameters parameters, Class entityClass, Class queryClass) {
        super(table, parameters, entityClass, queryClass);
    }

    @Override
    public Q distinct() {
        this.wrapperData.setDistinct(true);
        return (Q) this;
    }

    /**
     * 显式指定查询所有字段, 在join查询中有用
     *
     * @return
     */
    public Q selectAll() {
        this.allFields().stream().map(c -> alias(this.alias, c)).forEach(this::select);
        return (Q) this;
    }

    @Override
    public Q selectId() {
        if (this.primary() == null) {
            throw new FluentMybatisException("The primary key of in table[" + this.wrapperData.getTable() + "] was not found.");
        } else {
            return this.select(alias(this.alias, this.primary()));
        }
    }

    /**
     * 查询指定字段
     *
     * @param columns
     * @return
     */
    public Q select(String... columns) {
        if (If.notEmpty(columns)) {
            Stream.of(columns).filter(s -> notBlank(s)).forEach(this.wrapperData::addSelectColumn);
        }
        return (Q) this;
    }

    @Override
    public Q limit(int limit) {
        this.wrapperData.setPaged(new PagedOffset(0, limit));
        return (Q) this;
    }

    @Override
    public Q limit(int from, int limit) {
        this.wrapperData.setPaged(new PagedOffset(from, limit));
        return (Q) this;
    }

    public abstract List<String> allFields();
}