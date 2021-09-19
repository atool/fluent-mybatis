package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.functions.TableSupplier;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;

/**
 * IEntity 实体基类
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
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
     * 返回主键设置方法
     *
     * @return 主键设置方法
     */
    default Consumer pkSetter() {
        return null;
    }

    /**
     * 数据库实体对应的Entity类名称
     * 在具体的XyzEntity类中定义为final
     *
     * @return 实例类
     */
    Class<? extends IEntity> entityClass();

    /**
     * 将实体对象转换为map对象, 不包括空字段
     *
     * @return map对象
     */
    default Map<String, Object> toEntityMap() {
        return this.toEntityMap(false);
    }

    /**
     * 将实体对象转换为map对象
     *
     * @param allowedNull true:所有字段; false: 仅仅非空字段
     * @return map对象
     */
    default Map<String, Object> toEntityMap(boolean allowedNull) {
        return IRef.entityKit(this.entityClass()).toEntityMap(this, allowedNull);
    }

    /**
     * 将实体对象转换为数据库字段为key的map对象, 不包括空字段
     *
     * @return map对象
     */
    default Map<String, Object> toColumnMap() {
        return this.toColumnMap(false);
    }

    /**
     * 将实体对象转换为数据库字段为key的map对象
     *
     * @param allowNull true:仅仅非空字段; false: 所有字段
     * @return map对象
     */
    default Map<String, Object> toColumnMap(boolean allowNull) {
        return IRef.entityKit(this.entityClass()).toColumnMap(this, allowNull);
    }

    /**
     * 拷贝对象
     *
     * @param <E> 实例类型
     * @return 实例对象
     */
    default <E extends IEntity> E copy() {
        return IRef.entityKit(this.entityClass()).copy(this);
    }

    /**
     * 动态修改归属表, 默认无需设置
     * 只有在插入数据时, 不想使用默认对应的数据库表, 想动态调整时才需要
     *
     * @param supplier 动态归属表
     */
    default <E extends IEntity> E changeTableBelongTo(TableSupplier supplier) {
        return (E) this;
    }

    /**
     * 动态修改归属表, 默认无需设置
     * 只有在插入数据时, 不想使用默认对应的数据库表, 想动态调整时才需要
     *
     * @param supplier 动态归属表
     */
    default <E extends IEntity> E changeTableBelongTo(String supplier) {
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