package cn.org.atool.fluent.mybatis.base;

import java.io.Serializable;
import java.util.Map;

/**
 * IEntity 实体基类
 *
 * @author darui.wu
 */
public interface IEntity extends Serializable {
    /**
     * 返回实体主键
     *
     * @return 主键
     */
    default Serializable findPk() {
        return null;
    }

    /**
     * 数据库实体对应的Entity类名称, 在具体的XyzEntity类中定义为final, 防止返回匿名子类名称
     *
     * @return 实例类
     */
    default Class<? extends IEntity> entityClass() {
        return this.getClass();
    }

    /**
     * 将实体对象转换为map对象, 不包括空字段
     *
     * @return map对象
     */
    default Map<String, Object> toEntityMap() {
        return this.toEntityMap(true);
    }

    /**
     * 将实体对象转换为map对象
     *
     * @param isNoN true:仅仅非空字段; false: 所有字段
     * @return map对象
     */
    default Map<String, Object> toEntityMap(boolean isNoN) {
        return IRefs.findEntityHelper(this.entityClass()).toEntityMap(this, isNoN);
    }

    /**
     * 将实体对象转换为数据库字段为key的map对象, 不包括空字段
     *
     * @return map对象
     */
    default Map<String, Object> toColumnMap() {
        return this.toColumnMap(true);
    }

    /**
     * 将实体对象转换为数据库字段为key的map对象
     *
     * @param isNoN true:仅仅非空字段; false: 所有字段
     * @return map对象
     */
    default Map<String, Object> toColumnMap(boolean isNoN) {
        return IRefs.findEntityHelper(this.entityClass()).toColumnMap(this, isNoN);
    }

    /**
     * 拷贝对象
     *
     * @param <E> 实例类型
     * @return 实例对象
     */
    default <E extends IEntity> E copy() {
        return IRefs.findEntityHelper(this.entityClass()).copy(this);
    }

    /**
     * 动态修改归属表, 默认无需设置
     * 只有在插入数据时, 不想使用默认对应的数据库表, 想动态调整时才需要
     *
     * @param table 动态归属表
     */
    default <E extends IEntity> E changeTableBelongTo(String table) {
        return (E) this;
    }

    /**
     * 返回动态归属表
     *
     * @return 动态归属表
     */
    default String findTableBelongTo() {
        return null;
    }
}