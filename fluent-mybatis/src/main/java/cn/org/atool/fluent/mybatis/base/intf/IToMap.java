package cn.org.atool.fluent.mybatis.base.intf;

import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.metadata.GetterMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * 将对象转换为Map
 *
 * @author darui.wu
 */
public interface IToMap {
    /**
     * 实现对象转map操作
     *
     * @return Map<String, Object>
     */
    default Map<String, Object> toMap() {
        return toMap(this);
    }

    /**
     * 实现对象转map操作
     *
     * @param obj 目标对象
     * @return Map<String, Object>
     */
    static Map<String, Object> toMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        KeyMap<GetterMeta> metas = GetterMeta.get(obj.getClass());
        for (GetterMeta meta : metas.values()) {
            try {
                map.put(meta.fieldName, meta.getValue(obj));
            } catch (Exception e) {
                throw new RuntimeException("Error getting value of property[" + meta.fieldName + "] of object[" + obj.getClass().getName() + "].", e);
            }
        }
        return map;
    }
}