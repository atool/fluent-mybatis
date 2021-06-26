package cn.org.atool.fluent.mybatis.base.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.If.notNull;

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

    /**
     * 添加字段值
     *
     * @param field 字段
     * @param value 字段值
     * @param isNoN true: 非空字段, false: 允许null值
     * @return map实例
     */
    public EntityToMap put(FieldMapping field, Object value, boolean isNoN) {
        if (notNull(value) || !isNoN) {
            map.put(isProperty ? field.name : field.column, value);
        }
        return this;
    }
}
