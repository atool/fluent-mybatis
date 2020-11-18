package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.mapper.QueryExecutor;

import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * IEntityQuery: 查询接口
 *
 * @param <E> 对应的实体类
 * @param <Q> 查询器
 * @author darui.wu
 */
public interface IQuery<
    E extends IEntity,
    Q extends IQuery<E, Q>>
    extends IWrapper<E, Q, Q> {
    /**
     * distinct 查询
     *
     * @return self
     */
    Q distinct();

    /**
     * 查询Entity所有字段
     *
     * @return
     */
    Q selectAll();

    /**
     * 只查询主键字段
     *
     * @return self
     */
    Q selectId();

    /**
     * 设置limit值
     *
     * @param limit 最大查询数量
     * @return self
     */
    Q limit(int limit);

    /**
     * 设置limit值
     *
     * @param start 开始查询偏移量
     * @param limit 最大查询数量
     * @return self
     */
    Q limit(int start, int limit);

    default QueryExecutor<E> to() {
        Class entityClass = this.getWrapperData().getEntityClass();
        assertNotNull("entity class", entityClass);
        IRichMapper mapper = IRefs.instance().mapper(entityClass);
        return new QueryExecutor<E>(mapper, this);
    }

    default QueryExecutor<E> to(IRichMapper<E> mapper) {
        return new QueryExecutor<>(mapper, this);
    }

    /**
     * 执行查询操作
     *
     * @param executor 具体查询操作
     * @param <R>      结果类型
     * @return 结果
     * @deprecated replaced by {@link #to(IRichMapper)}
     */
    @Deprecated
    default <R> R execute(Function<Q, R> executor) {
        return executor.apply((Q) this);
    }
}