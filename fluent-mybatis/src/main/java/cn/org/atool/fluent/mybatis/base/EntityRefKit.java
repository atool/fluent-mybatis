package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.mybatis.functions.RefKey;
import cn.org.atool.fluent.mybatis.utility.RefKit;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.common.kits.StringKit.PRE_FIND;
import static cn.org.atool.fluent.mybatis.If.isEmpty;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.capitalFirst;

/**
 * 设置实例关联关系和数据工具类
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class EntityRefKit {
    /**
     * Entity指定
     *
     * @param entities 实例列表
     * @param getter   getter方法
     * @return 字段值列表
     */
    public static <E, R> Set<R> values(List<E> entities, Function<E, R> getter) {
        return entities.stream().map(getter).collect(Collectors.toSet());
    }

    /**
     * 根据RefKey信息, 设置srcEntities里里面的关联数据
     *
     * @param refKey      关联信息
     * @param srcEntities 原Entity列表
     * @param refEntities 被关联Entity列表
     */
    public static <E extends IEntity, R extends IEntity>
    void groupRelation(RefKey<E, R> refKey, List<E> srcEntities, List<R> refEntities) {
        Map<String, List<R>> map = toEntityMap(refEntities, refKey.ref);
        for (E entity : srcEntities) {
            String key = refKey.src.apply(entity);
            EntityRefKit.cache(entity, refKey.refMethodName, map.get(key), refKey.isList);
        }
    }

    /**
     * 将关联数据放到RichEntity的缓存中
     *
     * @param entity  RichEntity
     * @param keyName 关联缓存键值
     * @param list    关联数据
     * @param isList  关联数据为单个还是List
     */
    public static void cache(IEntity entity, String keyName, List list, boolean isList) {
        if (entity != null) {
            if (!(entity instanceof RichEntity)) {
                throw new IllegalStateException("In order to use @RefMethod methods, Entity[" + entity.entityClass().getName() + "] must extends RichEntity.");
            } else if (isList) {
                ((RichEntity) entity).cached(keyName, list == null ? Collections.emptyList() : list);
            } else {
                ((RichEntity) entity).cached(keyName, isEmpty(list) ? null : list.get(0));
            }
        }
    }

    /**
     * 将entities列表根据特征值归类
     *
     * @param entities 实例列表
     * @param keyFunc  特征值构造
     * @return 归类或的Map
     */
    public static <E> Map<String, List<E>> toEntityMap(List<E> entities, Function<E, String> keyFunc) {
        Map<String, List<E>> map = new HashMap<>();
        for (E e : entities) {
            String key = keyFunc.apply(e);
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(e);
        }
        return map;
    }

    public static Object findRefData(Class<IEntity> entityClass, IEntity entity, String method) {
        RefKey refKey = getRefKeyOfRefMethod(entityClass, method);
        return refKey == null ? null : refKey.finder.get(entity);
    }

    public static RefKey getRefKeyOfRefMethod(Class eClass, String methodName) {
        KeyMap<RefKey> keys = RefKit.byEntity(eClass).refKeys();
        if (keys.containsKey(methodName)) {
            return keys.get(methodName);
        } else if (!methodName.startsWith(PRE_FIND)) {
            String method = PRE_FIND + capitalFirst(methodName);
            return keys.get(method);
        } else {
            return null;
        }
    }
}