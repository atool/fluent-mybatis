package cn.org.atool.fluent.common.kits;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ParameterizedTypes: 泛型工具
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ParameterizedTypes {
    /**
     * 返回type有关declared通配符genericName的实际类型
     *
     * @param type        实际泛型类
     * @param declared    定义通配符的类
     * @param genericName 通配符
     * @param <T>         实际类型
     * @return 实际类型
     */
    public static <T> T getType(Type type, Class declared, String genericName) {
        Map<String, Object> map = getTypeMap(type, declared);
        return getDeclaredType(map, declared, genericName);
    }

    public static <T> T getDeclaredType(Map<String, Object> map, Type declared, String genericName) {
        String name = genericName + ":" + declared.toString();
        while (map.containsKey(name)) {
            Object value = map.get(name);
            if (value instanceof String) {
                name = (String) value;
            } else {
                return (T) value;
            }
        }
        return null;
    }

    /**
     * 返回泛型定义Type
     *
     * @param type     class 或者 ParameterizedType
     * @param declared 定义通配符的类
     * @return ParameterizedType
     */
    public static ParameterizedType toParameterizedType(Type type, Class declared) {
        if (type instanceof ParameterizedType) {
            return (ParameterizedType) type;
        }
        if (!(type instanceof Class) || Object.class.equals(type)) {
            return null;
        }
        final Class clazz = (Class) type;
        if (!declared.isAssignableFrom(clazz)) {
            return null;
        }
        if (clazz.getSuperclass() != null && declared.isAssignableFrom(clazz.getSuperclass())) {
            return toParameterizedType(clazz.getGenericSuperclass(), declared);
        }
        final Type[] genericInterfaces = clazz.getGenericInterfaces();
        for (Type gi : genericInterfaces) {
            if (gi instanceof Class && declared.isAssignableFrom((Class) gi)) {
                return toParameterizedType(gi, declared);
            }
            if (!(gi instanceof ParameterizedType)) {
                continue;
            }
            Type raw = ((ParameterizedType) gi).getRawType();
            if (raw instanceof Class && declared.isAssignableFrom((Class) raw)) {
                return toParameterizedType(gi, declared);
            }
        }
        return null;
    }

    /**
     * 返回泛型定义 通配符和实际类型关系
     *
     * @param type     Class or ParameterizedType
     * @param declared 定义通配符的类
     * @return key: 通配符+declaredClass -> value: 通配符+declaredClass 或 实际类型
     */
    public static Map getTypeMap(Type type, Class declared) {
        final Map<String, Object> typeMap = new HashMap<>();
        while (null != type) {
            final ParameterizedType parameterizedType = toParameterizedType(type, declared);
            if (null == parameterizedType) {
                break;
            }
            final Type[] typeArguments = parameterizedType.getActualTypeArguments();
            final Class rawType = (Class) parameterizedType.getRawType();
            final Type[] typeParameters = rawType.getTypeParameters();

            for (int index = 0; index < typeParameters.length; index++) {
                typeMap.put((String) typeName(typeParameters[index]), typeName(typeArguments[index]));
            }
            type = rawType;
        }
        return typeMap;
    }

    private static Object typeName(Type type) {
        if (type instanceof TypeVariable) {
            GenericDeclaration g = ((TypeVariable) type).getGenericDeclaration();
            return type.getTypeName() + ":" + g.toString();
        } else {
            return type;
        }
    }

    /**
     * 非Form Object对象
     *
     * @return true/false
     */
    public static boolean notFormObject(Class type) {
        if (type.isPrimitive() || type.isEnum() || type.getName().startsWith("java.")) {
            /* java自带的类型 */
            return true;
        } else if (Collection.class.isAssignableFrom(type) || type.isArray() || Map.class.isAssignableFrom(type)) {
            /* 数组, 集合, 字典 */
            return true;
        } else { /* 时间 */
            return Date.class.isAssignableFrom(type) || Temporal.class.isAssignableFrom(type);
        }
    }
}