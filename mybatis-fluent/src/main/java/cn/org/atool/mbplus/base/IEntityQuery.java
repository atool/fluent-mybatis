package cn.org.atool.mbplus.base;

import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;
import com.baomidou.mybatisplus.core.conditions.query.Query;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IEntityQuery<Q extends IEntityQuery, T> extends Compare<Q, String>, Query<Q, T, String> {
    /**
     * 设置limit值
     *
     * @param limit
     * @return
     */
    Q limit(int limit);

    /**
     * 设置limit值
     *
     * @param start
     * @param limit
     * @return
     */
    Q limit(int start, int limit);

    /**
     * distinct 查询
     *
     * @param columns
     * @return
     */
    Q distinct(String... columns);

    /**
     * 执行查询操作
     *
     * @param doIt 具体查询操作
     * @param <R1>
     * @return
     */
    default <R1> R1 doIt(Function<Q, R1> doIt) {
        return doIt.apply((Q) this);
    }

    /**
     * 执行查询操作
     *
     * @param doIt      具体查询操作
     * @param extractor 对查询结果处理
     * @param <R1>
     * @param <R2>
     * @return
     */
    default <R1, R2> R2 doIt(Function<Q, R1> doIt, Function<R1, R2> extractor) {
        return extractor.apply(doIt.apply((Q) this));
    }

    /**
     * 执行查询操作， 对返回列表元素逐一调用extractor处理
     *
     * @param doIt      具体查询操作
     * @param extractor 对查询结果处理
     * @param <R1>
     * @param <R2>
     * @return
     */
    default <R1, R2> List<R2> doItEach(Function<Q, List<R1>> doIt, Function<R1, R2> extractor) {
        return doIt.apply((Q) this).stream()
                .map(extractor::apply)
                .collect(Collectors.toList());
    }
}
