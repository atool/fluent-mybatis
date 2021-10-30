package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.IMethodAround;
import cn.org.atool.fluent.mybatis.base.IEntity;

import java.lang.reflect.Method;

/**
 * 无切面处理
 *
 * @author darui.wu
 */
public class NoMethodAround implements IMethodAround {
    public static IMethodAround instance = new NoMethodAround();

    private NoMethodAround() {
    }

    @Override
    public MethodMeta before(Class<? extends IEntity> entityClass, Method method, Object[] args) {
        return new MethodMeta(entityClass, method, args);
    }

    @Override
    public Object after(Class<? extends IEntity> entityClass, Method method, Object result) {
        return result;
    }
}