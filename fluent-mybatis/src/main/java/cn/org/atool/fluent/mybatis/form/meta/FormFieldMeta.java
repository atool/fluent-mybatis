package cn.org.atool.fluent.mybatis.form.meta;

import cn.org.atool.fluent.form.annotation.EntryType;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Method;

/**
 * FormFieldMeta: Form字段元数据
 *
 * @author darui.wu
 */
@ToString(of = "name")
@Getter
public class FormFieldMeta {
    /**
     * 字段名称
     */
    private final String name;
    /**
     * 类型
     */
    private final EntryType type;
    /**
     * getter方法
     */
    private final Method getter;
    /**
     * setter方法
     */
    private final Method setter;
    /**
     * 忽略空值情况
     */
    private final boolean ignoreNull;

    public FormFieldMeta(String name, EntryType type, Method getter, Method setter, boolean ignoreNull) {
        this.name = name;
        this.type = type;
        this.getter = getter;
        this.setter = setter;
        this.ignoreNull = ignoreNull;
    }

    /**
     * 返回字段值
     *
     * @param target Form对象
     * @return 字段值
     */
    public Object get(Object target) {
        if (getter == null) {
            return null;
        }
        try {
            return getter.invoke(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void set(Object target, Object value) {
        if (setter == null) {
            return;
        }
        try {
            setter.invoke(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}