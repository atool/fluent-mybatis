package cn.org.atool.fluent.mybatis.base.model;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Param_ET;

/**
 * update by entity字段构造
 *
 * @author wudarui
 */
public class UpdateSet {
    /**
     * 待更新记录字段表达式
     */
    @Getter
    private final List<String> updates = new ArrayList<>();

    /**
     * update字段表达式
     *
     * @param dbType   数据库类型
     * @param field    对象字段
     * @param value    对象属性值
     * @param _default insert默认值
     */
    public UpdateSet add(DbType dbType, FieldMapping field, Object value, String _default) {
        if (value != null) {
            this.updates.add(field.columnEqVar(dbType, Param_ET, null));
        } else if (notBlank(_default)) {
            this.updates.add(dbType.wrap(field.column) + " = " + _default);
        }
        return this;
    }
}