package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.common.kits.SegmentLocks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static cn.org.atool.fluent.common.kits.StringKit.PRE_SET;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.notBaseEntity;

/**
 * SetterMeta: setter方法元数据定义
 *
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
public class SetterMeta {
    private final String fieldName;

    private final Method method;

    public final Type fType;

    private SetterMeta(Method method) {
        this.method = method;
        String name = method.getName();
        this.fieldName = name.substring(3, 4).toLowerCase() + name.substring(4);
        this.fType = method.getGenericParameterTypes()[0];
    }

    public void setValue(Object target, Object value) throws InvocationTargetException, IllegalAccessException {
        this.method.invoke(target, value);
    }

    private static final KeyMap<KeyMap<SetterMeta>> methodMetas = new KeyMap<>();

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
    public static SetterMeta get(Class klass, String fieldName) {
        ClassLock.lockDoing(methodMetas::containsKey, klass, () -> methodMetas.put(klass, buildMetas(klass)));
        return methodMetas.get(klass).get(fieldName);
    }

    public static KeyMap<SetterMeta> get(Class klass) {
        ClassLock.lockDoing(methodMetas::containsKey, klass, () -> methodMetas.put(klass, buildMetas(klass)));
        return methodMetas.get(klass);
    }

    private static KeyMap<SetterMeta> buildMetas(Class klass) {
        KeyMap<SetterMeta> classMethods = new KeyMap<>();
        while (notBaseEntity(klass)) {
            for (Method m : klass.getDeclaredMethods()) {
                if (!m.getName().startsWith(PRE_SET) || m.getParameterCount() != 1) {
                    continue;
                }
                m.setAccessible(true);
                SetterMeta meta = new SetterMeta(m);
                classMethods.put(meta.fieldName, meta);
            }
            klass = klass.getSuperclass();
        }
        return classMethods;
    }
}