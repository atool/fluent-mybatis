package cn.org.atool.fluent.form;

import cn.org.atool.fluent.form.kits.NoMethodAround;
import cn.org.atool.fluent.form.meta.MethodMeta;
import cn.org.atool.fluent.mybatis.base.IEntity;

import java.lang.reflect.Method;

/**
 * 对form service接口进行切面处理
 *
 * @author wudarui
 */
@SuppressWarnings("unused")
public interface IMethodAround {
    /**
     * 根据方法定义构造方法元数据(从缓存获取)
     *
     * @param entityClass 执行的表Entity类
     * @param method      执行方法
     * @return 方法元数据
     */
    default MethodMeta cache(Class<? extends IEntity> entityClass, Method method) {
        String mName = method.toString();
        NoMethodAround.MethodLock.lockDoing(NoMethodAround.METHOD_METAS_CACHED::containsKey, mName, () -> NoMethodAround.METHOD_METAS_CACHED.put(mName, this.before(entityClass, method)));
        return NoMethodAround.METHOD_METAS_CACHED.get(mName);
    }

    /**
     * 根据方法定义构造方法元数据
     *
     * @param entityClass 执行的表Entity类
     * @param method      执行方法
     * @return 方法元数据
     */
    MethodMeta before(Class<? extends IEntity> entityClass, Method method);

    /**
     * 对入参进行预处理
     *
     * @param meta 方法元数据
     * @param args 原始入参
     * @return 处理后的入参
     */
    default Object[] before(MethodMeta meta, Object... args) {
        return args;
    }

    /**
     * 结果值处理
     *
     * @param entityClass 执行的表Entity类
     * @param method      执行方法
     * @param args        原始入参
     * @param result      FormService执行结果
     * @return 原始方法的返回值
     */
    Object after(Class<? extends IEntity> entityClass, Method method, Object[] args, Object result);

    /**
     * 异常值处理
     *
     * @param entityClass 执行的表Entity类
     * @param method      执行方法
     * @param args        原始入参
     * @param exception   FormService执行过程中抛出的异常
     * @return 原始方法的返回值
     */
    default Object after(Class<? extends IEntity> entityClass, Method method, Object[] args, RuntimeException exception) {
        throw exception;
    }
}