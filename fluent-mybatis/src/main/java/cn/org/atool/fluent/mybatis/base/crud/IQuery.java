package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.mapper.QueryExecutor;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.OrderByBase;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import cn.org.atool.fluent.mybatis.utility.RefKit;

import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * 查询构造器基类
 *
 * @param <E> 实体类型
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public interface IQuery<E extends IEntity> {
    /**
     * distinct 查询
     *
     * @return self
     */
    <Q extends IQuery<E>> Q distinct();

    /**
     * 查询Entity所有字段
     *
     * @return self
     */
    <Q extends IQuery<E>> Q selectAll();

    /**
     * 只查询主键字段
     *
     * @return self
     */
    <Q extends IQuery<E>> Q selectId();

    /**
     * 设置limit值
     *
     * @param limit 最大查询数量
     * @return self
     */
    <Q extends IQuery<E>> Q limit(int limit);

    /**
     * 设置limit值
     *
     * @param start 开始查询偏移量
     * @param limit 最大查询数量
     * @return self
     */
    <Q extends IQuery<E>> Q limit(int start, int limit);

    /**
     * 追加在sql语句的末尾
     * !!!慎用!!!
     * 有sql注入风险
     *
     * @param lastSql 追加SQL
     * @return self
     */
    <Q extends IQuery<E>> Q last(String lastSql);

    /**
     * 返回where
     *
     * @return self
     */
    WhereBase where();

    /**
     * 返回order排序
     *
     * @return
     */
    OrderByBase orderBy();

    /**
     * 返回查询器或更新器对应的xml数据
     * 系统方法, 请勿调用
     *
     * @return self
     */
    WrapperData data();

    /**
     * 根据Query定义执行后续操作
     * <pre>
     *   要使用本方法
     *   需要定义 {@link cn.org.atool.fluent.mybatis.spring.MapperFactory} spring bean
     *
     *   same as  {@link #of(IRichMapper)} 方法
     *  </pre>
     *
     * @return self
     */
    default QueryExecutor<E> to() {
        Class entityClass = ((BaseWrapper) this).getEntityClass();
        assertNotNull("entity class", entityClass);
        IRichMapper mapper = RefKit.mapper(entityClass);
        return new QueryExecutor<>(mapper, this);
    }

    /**
     * 根据Query定义执行后续操作
     *
     * @param mapper 执行操作的mapper
     * @return QueryExecutor
     */
    default QueryExecutor<E> of(IRichMapper<E> mapper) {
        return new QueryExecutor<>(mapper, this);
    }

    /**
     * 执行查询操作
     *
     * @param executor 具体查询操作
     * @param <R>      结果类型
     * @return 结果
     * @deprecated replaced by {@link #of(IRichMapper).method(...)}
     */
    @Deprecated
    default <R> R execute(Function<IQuery<E>, R> executor) {
        return executor.apply(this);
    }
}