package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.form.IMethodAround;
import cn.org.atool.fluent.form.meta.MethodMeta;

import java.lang.reflect.Method;

/**
 * 无切面处理
 *
 * @author darui.wu
 */
@SuppressWarnings("rawtypes")
public class NoMethodAround implements IMethodAround {
    public static IMethodAround instance = new NoMethodAround();

    private NoMethodAround() {
    }

    @Override
    public MethodMeta before(Class entityClass, Method method) {
        return MethodMeta.meta(entityClass, method);
    }

    @Override
    public Object after(Class entityClass, Method method, Object[] args, Object result) {
        return result;
    }
}