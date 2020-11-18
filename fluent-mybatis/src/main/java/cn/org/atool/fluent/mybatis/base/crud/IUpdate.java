package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.mapper.UpdaterExecutor;

import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

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
    NQ extends IQuery<E, NQ>>
    extends IWrapper<E, U, NQ> {
    /**
     * 设置limit值
     *
     * @param limit
     * @return
     */
    U limit(int limit);

    /**
     * 根据Updater定义执行后续操作
     *
     * @return
     */
    default UpdaterExecutor to() {
        Class entityClass = this.getWrapperData().getEntityClass();
        assertNotNull("entity class", entityClass);
        IRichMapper mapper = IRefs.instance().mapper(entityClass);
        return new UpdaterExecutor(mapper, this);
    }

    /**
     * 根据Updater定义执行后续操作
     *
     * @param mapper 执行操作的mapper
     * @return
     */
    default UpdaterExecutor of(IRichMapper<E> mapper) {
        return new UpdaterExecutor(mapper, this);
    }

    /**
     * 执行更新操作
     *
     * @param executor 具体更新操作
     * @return 返回更新的记录数
     * @deprecated replaced by {@link #of(IRichMapper)}
     */
    @Deprecated
    default int execute(Function<U, Integer> executor) {
        return executor.apply((U) this);
    }
}