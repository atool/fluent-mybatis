package cn.org.atool.fluent.mybatis.model.form;

import cn.org.atool.fluent.form.ItemType;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * FormFieldMeta: Form字段元数据
 *
 * @author darui.wu
 */
@Getter
public class FormFieldMeta {
    /**
     * 字段名称
     */
    private final String name;
    /**
     * 类型
     */
    private final ItemType type;
    /**
     * getter方法
     */
    private final Method method;
    /**
     * 忽略空值情况
     */
    private final boolean ignoreNull;

    public FormFieldMeta(String name, ItemType type, Method getter, boolean ignoreNull) {
        this.name = name;
        this.type = type;
        this.method = getter;
        this.ignoreNull = ignoreNull;
    }

    /**
     * 返回字段值
     *
     * @param target Form对象
     * @return 字段值
     */
    public Object get(Object target) {
        try {
            return method.invoke(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}