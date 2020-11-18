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
     * @return
     */
    default Class<? extends IEntity> entityClass() {
        return this.getClass();
    }

    /**
     * 将实体对象转换为map对象
     *
     * @return map对象
     */
    default Map<String, Object> toEntityMap() {
        return IRefs.findEntityHelper(this.entityClass()).toEntityMap(this);
    }

    /**
     * 将实体对象转换为数据库字段为key的map对象
     *
     * @return map对象
     */
    default Map<String, Object> toColumnMap() {
        return IRefs.findEntityHelper(this.entityClass()).toColumnMap(this);
    }

    /**
     * 拷贝对象
     *
     * @param <E>
     * @return
     */
    default <E extends IEntity> E copy() {
        return (E) IRefs.findEntityHelper(this.entityClass()).copy(this);
    }
}