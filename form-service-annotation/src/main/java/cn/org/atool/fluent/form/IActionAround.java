package cn.org.atool.fluent.form;

import cn.org.atool.fluent.form.meta.ActionMeta;

import java.lang.reflect.Method;

/**
 * 对form service接口进行切面处理
 *
 * @author wudarui
 */
public interface IActionAround {
    /**
     * 入参处理
     *
     * @param entityClass 执行的表Entity类
     * @param method      执行方法
     * @param args        原始入参
     * @return 处理过的入参
     */
    ActionMeta before(Class entityClass, Method method, Object[] args);

    /**
     * 结果值处理
     *
     * @param entityClass 执行的表Entity类
     * @param method      执行方法
     * @param result      FormService执行结果
     * @return 原始方法的返回值
     */
    Object after(Class entityClass, Method method, Object result);
}