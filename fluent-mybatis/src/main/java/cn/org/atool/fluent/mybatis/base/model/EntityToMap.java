package cn.org.atool.fluent.mybatis.base.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Entity对象转换为Map对象工具类
 *
 * @author wudarui
 */
public class EntityToMap {
    /**
     * 是否属性字段
     */
    private final boolean isProperty;

    @Getter
    private final Map<String, Object> map = new HashMap<>();

    public EntityToMap(boolean isProperty) {
        this.isProperty = isProperty;
    }

    public EntityToMap put(FieldMapping field, Object value) {
        if (value != null) {
            map.put(isProperty ? field.name : field.column, value);
        }
        return this;
    }
}
