package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.common.kits.SegmentLocks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static cn.org.atool.fluent.common.kits.StringKit.PRE_GET;
import static cn.org.atool.fluent.common.kits.StringKit.PRE_IS;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.notBaseEntity;

/**
 * getter方法元数据
 *
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
public class GetterMeta {
    public final String fieldName;

    private final Method method;

    public final Type fType;

    private GetterMeta(Method method) {
        this.method = method;
        String name = method.getName();
        if (name.startsWith(PRE_IS)) {
            this.fieldName = name.substring(2, 3).toLowerCase() + name.substring(3);
        } else {
            this.fieldName = name.substring(3, 4).toLowerCase() + name.substring(4);
        }
        this.fType = method.getGenericReturnType();
    }

    public Object getValue(Object target) throws InvocationTargetException, IllegalAccessException {
        return target == null ? null : this.method.invoke(target);
    }

    private static final KeyMap<KeyMap<GetterMeta>> methodMetas = new KeyMap<>();

    /**
     * 按class类进行加锁
     */
    private final static SegmentLocks<Class> ClassLock = new SegmentLocks<>(16);

    /**
     * 返回类klass属性setter方法
     *
     * @param klass     指定类
     * @param fieldName 属性名称
     * @return SetterMethodMeta
     */
    public static GetterMeta get(Class klass, String fieldName) {
        ClassLock.lockDoing(methodMetas::containsKey, klass, () -> methodMetas.put(klass, buildMetas(klass)));
        return methodMetas.get(klass).get(fieldName);
    }

    public static KeyMap<GetterMeta> get(Class klass) {
        ClassLock.lockDoing(methodMetas::containsKey, klass, () -> methodMetas.put(klass, buildMetas(klass)));
        return methodMetas.get(klass);
    }

    private static KeyMap<GetterMeta> buildMetas(Class klass) {
        KeyMap<GetterMeta> classMethods = new KeyMap<>();
        while (notBaseEntity(klass)) {
            for (Method m : klass.getDeclaredMethods()) {
                String name = m.getName();
                if (!name.startsWith(PRE_GET) && !name.startsWith(PRE_IS) || m.getParameterCount() != 0) {
                    continue;
                }
                m.setAccessible(true);
                GetterMeta meta = new GetterMeta(m);
                classMethods.put(meta.fieldName, meta);
            }
            klass = klass.getSuperclass();
        }
        return classMethods;
    }
}