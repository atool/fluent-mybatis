package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.TableSupplier;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * IEntity 实体基类
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public interface IEntity extends Serializable {
    /**
     * 返回实体主键
     *
     * @return 主键
     */
    default Serializable findPk() {
        FieldMapping f = RefKit.byEntity(this.entityClass()).primaryMapping();
        return f == null ? null : (Serializable) f.getter.get(this);
    }

    /**
     * 数据库实体对应的Entity类名称
     * 在具体的XyzEntity类中定义为final
     *
     * @return 实例类
     */
    default Class<? extends IEntity> entityClass() {
        return MybatisUtil.entityClass(this.getClass());
    }

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
        return RefKit.entityKit(this.entityClass()).toEntityMap(this, allowedNull);
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
        return RefKit.entityKit(this.entityClass()).toColumnMap(this, allowNull);
    }

    /**
     * 拷贝对象
     *
     * @param <E> 实例类型
     * @return 实例对象
     */
    default <E extends IEntity> E copy() {
        return RefKit.entityKit(this.entityClass()).copy(this);
    }

    /**
     * 动态修改归属表, 默认无需设置
     * 只有在插入数据时, 不想使用默认对应的数据库表, 想动态调整时才需要
     *
     * @param supplier 动态归属表
     */
    default <E extends IEntity> E tableSupplier(TableSupplier supplier) {
        return (E) this;
    }

    /**
     * 动态修改归属表, 默认无需设置
     * 只有在插入数据时, 不想使用默认对应的数据库表, 想动态调整时才需要
     *
     * @param supplier 动态归属表
     */
    default <E extends IEntity> E tableSupplier(String supplier) {
        return (E) this;
    }

    /**
     * 返回动态归属表
     *
     * @return 动态归属表
     */
    default String tableSupplier() {
        return null;
    }

    /**
     * 获取entity的属性field值
     *
     * @param field 属性名称
     * @param <T>   属性值类型
     * @return 属性值
     */
    default <T> T valueByField(String field) {
        return RefKit.entityKit(this.entityClass()).valueByField(this, field);
    }

    /**
     * 获取entity的对应数据库字段的属性值
     *
     * @param column 数据库字段名称
     * @param <T>    属性值类型
     * @return 属性值
     */
    default <T> T valueByColumn(String column) {
        return RefKit.entityKit(this.entityClass()).valueByColumn(this, column);
    }

    /**
     * 获取entity的对应字段的属性值
     *
     * @param fieldMapping 字段映射
     * @param <T>          属性值类型
     * @return 属性值
     */
    default <T> T valueBy(FieldMapping fieldMapping) {
        return (T) fieldMapping.getter.get(this);
    }
}