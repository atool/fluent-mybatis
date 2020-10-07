package cn.org.atool.fluent.mybatis.base;

import java.util.Map;

/**
 * entity帮助类
 *
 * @param <T>
 * @author wudarui
 */
public interface IEntityHelper<T extends IEntity> {
    /**
     * entity对象转换为map对象
     * key值为entity的属性字段名
     *
     * @param entity
     * @return
     */
    Map<String, Object> toEntityMap(T entity);

    /**
     * entity对象转换为map对象
     * key值为对应的数据库表字段名
     *
     * @param entity
     * @return
     */
    Map<String, Object> toColumnMap(T entity);
}