package cn.org.atool.fluent.mybatis.util;

import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 反射工具类
 *
 * @author darui.wu
 */
public class ReflectUtils {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPE_MAP = new IdentityHashMap<>(8);

    static {
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Boolean.class, boolean.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Byte.class, byte.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Character.class, char.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Double.class, double.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Float.class, float.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Integer.class, int.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Long.class, long.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Short.class, short.class);
    }

    /**
     * 代理 class 的名称
     */
    private static final List<String> PROXY_CLASS_NAMES = Arrays.asList(
        // cglib
        "net.sf.cglib.proxy.Factory",
        "org.springframework.cglib.proxy.Factory",
        // javassist
        "javassist.util.proxy.ProxyObject",
        "org.apache.ibatis.javassist.util.proxy.ProxyObject"
    );

    /**
     * <p>
     * 获取该类的所有属性列表
     * </p>
     *
     * @param clazz 反射类
     */
    public static List<Field> getFieldList(Class<?> clazz) {
        Map<String, Field> map = new HashMap<>();
        while (clazz.getSuperclass() != null && clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                if (!map.containsKey(fieldName) && !isStatic(field)) {
                    map.put(fieldName, field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return new ArrayList<>(map.values());
    }

    /**
     * 过滤 static 和 transient 关键字修饰的属性
     *
     * @param field
     * @return
     */
    private static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers());
    }

    /**
     * 获取字段get 或 is 方法
     *
     * @param cls   class
     * @param field 字段
     * @return Get方法
     */
    public static Method getMethod(Class<?> cls, Field field) {
        try {
            String methodName = (field.getType().equals(boolean.class) ? "is" : "get") + StringUtils.capitalFirst(field.getName());
            return cls.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw FluentMybatisException.instance("Error: NoSuchMethod in %s.  Cause:", e, cls.getName());
        }
    }

    /**
     * 判断是否为基本类型或基本包装类型
     *
     * @param clazz class
     * @return 是否基本类型或基本包装类型
     */
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        SimpleAssert.notNull(clazz, "Class must not be null");
        return (clazz.isPrimitive() || PRIMITIVE_WRAPPER_TYPE_MAP.containsKey(clazz));
    }


    /**
     * <p>
     * 判断是否为代理对象
     * </p>
     *
     * @param clazz 传入 class 对象
     * @return 如果对象class是代理 class，返回 true
     */
    public static boolean isProxy(Class<?> clazz) {
        if (clazz != null) {
            for (Class<?> cls : clazz.getInterfaces()) {
                if (PROXY_CLASS_NAMES.contains(cls.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * 获取当前对象的 class
     * </p>
     *
     * @param clazz 传入
     * @return 如果是代理的class，返回父 class，否则返回自身
     */
    public static Class<?> getProxyTargetClass(Class<?> clazz) {
        return isProxy(clazz) ? clazz.getSuperclass() : clazz;
    }
}