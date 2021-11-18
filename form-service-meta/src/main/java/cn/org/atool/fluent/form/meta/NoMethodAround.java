package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.IMethodAround;
import cn.org.atool.fluent.mybatis.base.IEntity;

import java.lang.reflect.Method;
import java.util.stream.IntStream;

/**
 * 无切面处理
 *
 * @author darui.wu
 */
public class NoMethodAround implements IMethodAround {
    public static IMethodAround instance = new NoMethodAround();

    public static final int lock_size = 20;
    /**
     * 分段锁对象
     */
    public static final Object[] LOCKS = IntStream.range(0, lock_size)
        .mapToObj(i -> new Object()).toArray();

    private NoMethodAround() {
    }

    @Override
    public MethodMeta before(Class<? extends IEntity> entityClass, Method method) {
        return new MethodMeta(entityClass, method);
    }

    @Override
    public Object after(Class<? extends IEntity> entityClass, Method method, Object[] args, Object result) {
        return result;
    }
}