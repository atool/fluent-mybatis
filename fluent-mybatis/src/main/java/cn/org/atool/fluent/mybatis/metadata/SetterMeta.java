package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.spring.IConvertor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

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

    private static final KeyMap<IConvertor> convertors = new KeyMap<>();

    private static final KeyMap<KeyMap<SetterMeta>> methodMetas = new KeyMap<>();

    /**
     * 返回类klass属性setter方法
     *
     * @param klass     指定类
     * @param fieldName 属性名称
     * @return SetterMethodMeta
     */
    public static SetterMeta get(Class klass, String fieldName) {
        if (!methodMetas.containsKey(klass)) {
            buildMetas(klass);
        }
        return methodMetas.get(klass).get(fieldName);
    }

    public static KeyMap<SetterMeta> get(Class klass) {
        if (!methodMetas.containsKey(klass)) {
            buildMetas(klass);
        }
        return methodMetas.get(klass);
    }

    private synchronized static void buildMetas(Class klass) {
        if (methodMetas.containsKey(klass)) {
            return;
        }
        Method[] methods = klass.getDeclaredMethods();
        KeyMap<SetterMeta> classMethods = new KeyMap<>();
        for (Method m : methods) {
            if (!m.getName().startsWith("set") || m.getParameterCount() != 1) {
                continue;
            }
            m.setAccessible(true);
            SetterMeta meta = new SetterMeta(m);
            classMethods.put(meta.fieldName, meta);
        }
        methodMetas.put(klass, classMethods);
    }

    public static IConvertor findConvertor(Type type) {
        return convertors.get(type.getTypeName());
    }

    public static void register(Type type, IConvertor convertor) {
        convertors.put(type.getTypeName(), convertor);
    }

    public static void register(String typeName, IConvertor convertor) {
        convertors.put(typeName, convertor);
    }
}