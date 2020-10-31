package cn.org.atool.fluent.mybatis.base.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 追加更新默认值
 *
 * @author wudarui
 */
public class UpdateDefault {
    /**
     * 待追加更新默认值列表
     */
    @Getter
    private final List<String> updateDefaults = new ArrayList<>();
    /**
     * 显式指定的更新字段列表
     */
    private final Map<String, String> updates;

    public UpdateDefault(Map<String, String> updates) {
        this.updates = updates;
    }

    /**
     * 增加待追加更新的默认值
     * 如果没有显式指定更新，则追加更新默认值
     *
     * @param field
     * @param _default
     * @return
     */
    public UpdateDefault add(FieldMapping field, String _default) {
        if (!updates.containsKey(field.name)) {
            updateDefaults.add(field.column + " = " + _default);
        }
        return this;
    }
}
