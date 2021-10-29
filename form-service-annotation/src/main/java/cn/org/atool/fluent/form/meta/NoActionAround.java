package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.IActionAround;

import java.lang.reflect.Method;

/**
 * 无切面处理
 *
 * @author darui.wu
 */
public class NoActionAround implements IActionAround {
    public static IActionAround instance = new NoActionAround();

    private NoActionAround() {
    }

    @Override
    public ActionMeta before(Class entityClass, Method method, Object[] args) {
        return new ActionMeta(entityClass, method, args);
    }

    @Override
    public Object after(Class entityClass, Method method, Object result) {
        return result;
    }
}