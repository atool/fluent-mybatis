package cn.org.atool.fluent.mybatis.base.model;

import cn.org.atool.fluent.mybatis.metadata.DbType;
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
     * @param field    字段
     * @param _default 默认值
     * @return UpdateDefault
     */
    public UpdateDefault add(DbType dbType, FieldMapping field, String _default) {
        String column = field.column;
        String wrap = dbType == null ? column : dbType.wrap(column);
        if (!updates.containsKey(column) && !updates.containsKey(wrap)) {
            updateDefaults.add(wrap + " = " + _default);
        }
        return this;
    }
}
