package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.condition.interfaces.Compare;
import cn.org.atool.fluent.mybatis.condition.interfaces.Update;

import java.util.function.Function;

public interface IEntityUpdate<U extends IEntityUpdate> extends Compare<U, String>, Update<U, String> {
    /**
     * 设置limit值
     *
     * @param limit
     * @return
     */
    U limit(int limit);

    /**
     * 设置limit值
     *
     * @param start
     * @param limit
     * @return
     */
    U limit(int start, int limit);

    /**
     * 执行更新操作
     *
     * @param doIt 具体更新操作
     * @return 返回更新的记录数
     */
    default int doIt(Function<U, Integer> doIt) {
        return doIt.apply((U) this);
    }
}