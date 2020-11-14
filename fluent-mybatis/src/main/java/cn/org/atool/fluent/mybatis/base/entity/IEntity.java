package cn.org.atool.fluent.mybatis.base.entity;

import cn.org.atool.fluent.mybatis.base.IRefs;

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
        throw new RuntimeException("not implement");
    }

    /**
     * 将实体对象转换为map对象
     *
     * @return map对象
     */
    default Map<String, Object> toEntityMap() {
        return IRefs.findEntityHelper(this.getClass()).toEntityMap(this);
    }

    /**
     * 将实体对象转换为数据库字段为key的map对象
     *
     * @return map对象
     */
    default Map<String, Object> toColumnMap() {
        return IRefs.findEntityHelper(this.getClass()).toColumnMap(this);
    }

    /**
     * 拷贝对象
     *
     * @param <E>
     * @return
     */
    default <E extends IEntity> E copy() {
        return (E) IRefs.findEntityHelper(this.getClass()).copy(this);
    }
}