package cn.org.atool.fluent.mybatis.model;

import java.util.HashMap;
import java.util.Map;

/**
 * key: 条件字段
 * value: 更新字段
 *
 * @author wudarui
 */
public class UpdateWhereMap extends HashMap<Map<String, Object>, Map<String, Object>> {
    public UpdateWhereMap add(Map<String, Object> where, Map<String, Object> update) {
        this.put(where, update);
        return this;
    }

    public static class StrKeyMap extends HashMap<String, Object> {
    }
}
