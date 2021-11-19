package cn.org.atool.fluent.form.kits;

import cn.org.atool.fluent.form.IMethodAround;
import cn.org.atool.fluent.form.meta.MethodMeta;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.utility.LockKit;

import java.lang.reflect.Method;

/**
 * 无切面处理
 *
 * @author darui.wu
 */
public class NoMethodAround implements IMethodAround {
    public static IMethodAround instance = new NoMethodAround();

    public static KeyMap<MethodMeta> METHOD_METAS_CACHED = new KeyMap<>();
    /**
     * 按 Method.toString() 签名进行加锁
     */
    public final static LockKit<String> MethodLock = new LockKit<>(16);

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