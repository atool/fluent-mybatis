package cn.org.atool.mybatis.fluent.base;

import java.io.Serializable;
import java.util.Map;

/**
 * @param <T>
 */
public interface IEntity<T> extends Serializable {
    /**
     * 返回实体主键
     *
     * @return 主键
     */
    Serializable findPk();

    /**
     * 将实体对象转换为map对象
     *
     * @return map对象
     */
    Map<String, Object> toMap();
}
