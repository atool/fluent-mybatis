package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IToMap;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.functions.MapFunction;
import cn.org.atool.fluent.mybatis.metadata.SetterMeta;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;
import cn.org.atool.fluent.mybatis.spring.IConvertor;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static java.util.stream.Collectors.toList;

/**
 * PoJo转换工具类
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public class PoJoHelper {
    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param list      Map或Entity对象列表
     * @param converter 转换函数
     * @param <POJO>    PoJo类型
     * @return 转换后的对象列表
     */
    public static <POJO> List<POJO> toPoJoList(List<Map<String, Object>> list, MapFunction<POJO> converter) {
        return list == null ? null : list.stream().map(map -> toPoJo(map, converter)).collect(toList());
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param map       map或entity对象
     * @param converter 转换函数
     * @param <POJO>    PoJo类型
     * @return 转换后的对象
     */
    public static <POJO> POJO toPoJo(Map<String, Object> map, @NonNull MapFunction<POJO> converter) {
        return map == null ? null : converter.apply(map);
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param clazz  POJO类型
     * @param list   map对象列表
     * @param <POJO> POJO类型
     * @return POJO实例列表
     */
    public static <POJO> List<POJO> toPoJoList(Class<POJO> clazz, List<Map<String, Object>> list) {
        return list == null ? null : list.stream().map(map -> toPoJo(clazz, map)).collect(toList());
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param clazz  POJO类型
     * @param list   map对象列表
     * @param <POJO> POJO类型
     * @return POJO实例列表
     */
    public static <POJO> List<POJO> toPoJoListIgnoreNotFound(Class<POJO> clazz, List<Map<String, Object>> list) {
        return list == null ? null : list.stream().map(map -> toPoJoIgnoreNotFound(clazz, map)).collect(toList());
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param clazz  POJO类型
     * @param map    map对象
     * @param <POJO> PoJo类型
     * @return 根据Map值设置后的对象
     */
    public static <POJO> POJO toPoJo(@NonNull Class<POJO> clazz, @NonNull Map<String, Object> map) {
        return toPoJo(clazz, map, false);
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param clazz  POJO类型
     * @param map    map对象
     * @param <POJO> PoJo类型
     * @return 根据Map值设置后的对象
     */
    public static <POJO> POJO toPoJoIgnoreNotFound(@NonNull Class<POJO> clazz, @NonNull Map<String, Object> map) {
        return toPoJo(clazz, map, true);
    }

    private static <POJO> POJO toPoJo(@NonNull Class<POJO> klass, @NonNull Map<String, Object> map, boolean ignoreNotFound) {
        POJO target = newInstance(klass);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String name = MybatisUtil.underlineToCamel(entry.getKey(), false);
            SetterMeta meta = SetterMeta.get(klass, name);
            if (meta == null) {
                if (ignoreNotFound) {
                    continue;
                }
                throw new RuntimeException("property[" + name + "] of class[" + klass.getName() + "] not found.");
            }
            try {
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                if (meta.fType instanceof Class) {
                    if (((Class) meta.fType).isAssignableFrom(value.getClass())) {
                        meta.setValue(target, value);
                    } else {
                        setDefaultType(meta, target, value);
                    }
                } else {
                    IConvertor convertor = SetterMeta.findConvertor(meta.fType);
                    if (convertor != null) {
                        value = convertor.get(value);
                    }
                    meta.setValue(target, value);
                }
            } catch (Exception e) {
                String err = String.format("convert map to object[class=%s, property=%s, type=%s] error: %s",
                    klass.getName(), name, meta.fType.toString(), e.getMessage());
                throw new RuntimeException(err, e);
            }
        }
        return target;
    }

    private static <POJO> POJO newInstance(Class<POJO> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("convert map to object[type=" + clazz.getName() + "] error: " + e.getMessage(), e);
        }
    }

    /**
     * 将object对象转换为map
     *
     * @param object 转换对象
     * @return Map
     */
    public static Map toMap(Object object) {
        assertNotNull("object", object);
        if (object instanceof IEntity) {
            return ((IEntity) object).toEntityMap();
        } else if (object instanceof Map) {
            return new HashMap((Map) object);
        } else if (object instanceof IToMap) {
            return ((IToMap) object).toMap();
        } else {
            return IToMap.toMap(object);
        }
    }

    /**
     * 进行默认类型的转换
     *
     * @param meta   setter方法
     * @param target 目标对象
     * @param value  属性值
     */
    private static void setDefaultType(SetterMeta meta, Object target, Object value) throws InvocationTargetException, IllegalAccessException {
        Class type = (Class) meta.fType;
        if (type == Long.class) {
            meta.setValue(target, Long.parseLong(value.toString()));
        } else if (type == Integer.class) {
            meta.setValue(target, Integer.parseInt(value.toString()));
        } else if (type == Boolean.class) {
            meta.setValue(target, ObjectArray.toBoolean(value));
        } else if (type == BigDecimal.class) {
            meta.setValue(target, new BigDecimal(value.toString()));
        } else if (type == BigInteger.class) {
            meta.setValue(target, new BigInteger(value.toString()));
        } else if (value instanceof Timestamp) {
            Timestamp t = (Timestamp) value;
            if (type == LocalDateTime.class) {
                meta.setValue(target, t.toLocalDateTime());
            } else if (type == LocalDate.class) {
                meta.setValue(target, LocalDate.from(t.toInstant().atZone(ZoneOffset.systemDefault())));
            } else if (type == LocalTime.class) {
                meta.setValue(target, LocalTime.from(t.toInstant().atZone(ZoneOffset.systemDefault())));
            } else {
                meta.setValue(target, value);
            }
        } else {
            meta.setValue(target, value);
        }
    }

    /**
     * 校验marker方式分页的分页参数合法性
     *
     * @param query 查询条件
     * @return 最大查询数
     */
    public static int validateTagPaged(IQuery query) {
        PagedOffset paged = query.data().paged();
        if (paged == null) {
            throw new FluentMybatisException("Paged parameter not set");
        }
        if (paged.getOffset() != 0) {
            throw new FluentMybatisException("The offset of TagList should from zero, please use method: limit(size) or limit(0, size) .");
        }
        return paged.getLimit();
    }

    /**
     * 返回list对象属性值数组
     *
     * @param list    对象列表
     * @param getFunc 获取list元素对应属性方法
     * @param <T>     转换对象类型
     * @return 属性值数组
     */
    public static <T> Object[] getFields(List<T> list, Function<T, Object> getFunc) {
        return list.stream().map(getFunc).toArray(Object[]::new);
    }
}