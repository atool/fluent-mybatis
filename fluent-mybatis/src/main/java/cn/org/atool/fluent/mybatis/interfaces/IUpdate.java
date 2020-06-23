package cn.org.atool.fluent.mybatis.interfaces;

import java.util.function.Function;

/**
 * IEntityUpdate: 更新接口
 *
 * @param <E> 对应的实体类
 * @param <U> 更新器
 * @param <Q> 对应的嵌套查询器
 * @author darui.wu
 */
public interface IUpdate<
    E extends IEntity,
    U extends IWrapper<E, U, Q>,
    Q extends IQuery<E, Q>
    >
    extends IWrapper<E, U, Q> {
    /**
     * 设置limit值
     *
     * @param limit
     * @return
     */
    U limit(int limit);

    /**
     * 执行更新操作
     *
     * @param executor 具体更新操作
     * @return 返回更新的记录数
     */
    default int execute(Function<U, Integer> executor) {
        return executor.apply((U) this);
    }
}