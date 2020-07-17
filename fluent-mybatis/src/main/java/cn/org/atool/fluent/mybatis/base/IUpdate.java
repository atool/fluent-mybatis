package cn.org.atool.fluent.mybatis.base;

import java.util.function.Function;

/**
 * IEntityUpdate: 更新接口
 *
 * @param <E>  对应的实体类
 * @param <U>  更新器
 * @param <NQ> 对应的嵌套查询器
 * @author darui.wu
 */
public interface IUpdate<
    E extends IEntity,
    U extends IWrapper<E, U, NQ>,
    NQ extends IQuery<E, NQ>
    >
    extends IWrapper<E, U, NQ> {

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