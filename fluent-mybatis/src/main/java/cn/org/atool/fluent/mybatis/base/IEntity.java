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
        throw new RuntimeException("not implement");
    }

    /**
     * 将实体对象转换为map对象
     *
     * @return map对象
     */
    default Map<String, Object> toMap() {
        throw new RuntimeException("not implement");
    }

    /**
     * 将实体对象转换为数据库字段为key的map对象
     *
     * @return map对象
     */
    default Map<String, Object> columnMap() {
        throw new RuntimeException("not implement");
    }
}