package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.entity.IEntity;

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