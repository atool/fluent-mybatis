package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.functions.MapFunction;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;
import lombok.NonNull;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;

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
     * @param map    map对象
     * @param <POJO> PoJo类型
     * @return 根据Map值设置后的对象
     */
    public static <POJO> POJO toPoJo(@NonNull Class<POJO> clazz, @NonNull Map<String, Object> map) {
        try {
            POJO target = clazz.getDeclaredConstructor().newInstance();
            MetaObject metaObject = MetaObject.forObject(target, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String name = MybatisUtil.underlineToCamel(entry.getKey(), false);

                Object value = entry.getValue();
                if (value == null) {
                    metaObject.setValue(name, null);
                }
                Class<?> type = metaObject.getSetterType(name);
                if (type.isAssignableFrom(value.getClass())) {
                    metaObject.setValue(name, value);
                } else {
                    setDefaultType(type, metaObject, name, value);
                }
            }
            return target;
        } catch (Exception e) {
            throw new RuntimeException("convert map to object[type=" + clazz.getName() + "] error: " + e.getMessage(), e);
        }
    }

    /**
     * 将object对象转换为map
     *
     * @param object
     * @return
     */
    public static Map toMap(Object object) {
        assertNotNull("object", object);
        Map map = new HashMap();
        if (object instanceof IEntity) {
            map = ((IEntity) object).toEntityMap();
        } else if (object instanceof Map) {
            map.putAll((Map) object);
        } else {
            MetaObject metaObject = MetaObject.forObject(object, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
            for (String field : metaObject.getGetterNames()) {
                map.put(field, metaObject.getValue(field));
            }
        }
        return map;
    }

    /**
     * 进行默认类型的转换
     *
     * @param type
     * @param metaObject
     * @param name
     * @param value
     */
    private static void setDefaultType(Class type, MetaObject metaObject, String name, Object value) {
        if (type == Long.class) {
            metaObject.setValue(name, Long.parseLong(value.toString()));
        } else if (type == Integer.class) {
            metaObject.setValue(name, Integer.parseInt(value.toString()));
        } else {
            metaObject.setValue(name, value);
        }
    }

    /**
     * 校验marker方式分页的分页参数合法性
     *
     * @param query 查询条件
     * @return 最大查询数
     */
    public static int validateTagPaged(IQuery query) {
        PagedOffset paged = query.getWrapperData().getPaged();
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
     * @param list
     * @param getFunc 获取list元素对应属性方法
     * @param <T>
     * @return 属性值数组
     */
    public static <T> Object[] getFields(List<T> list, Function<T, Object> getFunc) {
        return list.stream().map(getFunc).toArray(Object[]::new);
    }
}