package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * IEntityQuery: 查询接口
 *
 * @param <E> 对应的实体类
 * @param <Q> 查询器
 * @author darui.wu
 */
public interface IQuery<
    E extends IEntity,
    Q extends IQuery<E, Q>
    >
    extends IWrapper<E, Q, Q> {
    /**
     * distinct 查询
     *
     * @return self
     */
    Q distinct();

    /**
     * 只查询主键字段
     *
     * @return self
     */
    Q selectId();

    /**
     * 设置查询字段
     *
     * @param columns 字段列表
     * @return children
     */
    Q select(String... columns);

    /**
     * 设置查询字段
     *
     * @param fields 字段列表
     * @return children
     */
    Q select(FieldMapping... fields);

    /**
     * 过滤查询的字段信息
     *
     * <p>例1: 只要 java 字段名以 "test" 开头的   -> select(i -> i.getProperty().startsWith("test"))</p>
     * <p>例2: 要全部字段                        -> select(i -> true)</p>
     * <p>例3: 只要字符串类型字段                 -> select(i -> i.getPropertyType instance String)</p>
     *
     * @param includePrimary 包含主键?
     * @param predicate 过滤方式 (主键除外!)
     * @return 字段选择器
     */
    Q select(boolean includePrimary, FieldPredicate predicate);

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

    /**
     * 执行查询操作
     *
     * @param executor 具体查询操作
     * @param <R>      结果类型
     * @return 结果
     */
    default <R> R execute(Function<Q, R> executor) {
        return executor.apply((Q) this);
    }

    /**
     * 执行查询操作， 对返回列表元素逐一调用extractor处理
     *
     * @param executor  具体查询操作
     * @param extractor 对查询结果处理
     * @param <R>
     * @param <R2>
     * @return ignore
     */
    default <R, R2> List<R2> execute(Function<Q, List<R>> executor, Function<R, R2> extractor) {
        List<R> list = executor.apply((Q) this);
        return list.stream().map(extractor::apply).collect(Collectors.toList());
    }
}