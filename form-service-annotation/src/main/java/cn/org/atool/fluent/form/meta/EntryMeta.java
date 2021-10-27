package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.EntryType;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * FormFieldMeta: Form字段元数据
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
@ToString(of = "name")
@Getter
public class EntryMeta {
    /**
     * 字段名称
     */
    private final String name;
    /**
     * 类型
     */
    private final EntryType type;
    /**
     * getter方法名称
     */
    private final String getterName;
    /**
     * getter方法
     */
    private final Function getter;
    /**
     * setter方法名称
     */
    private final String setterName;
    /**
     * setter方法
     */
    private final BiConsumer setter;
    /**
     * 忽略空值情况
     */
    private final boolean ignoreNull;

    public EntryMeta(String name, EntryType type, Method getter, Method setter, boolean ignoreNull) {
        this.name = name;
        this.type = type;
        this.getter = getter == null ? null : target -> getValue(getter, target);
        this.getterName = getter == null ? name : getter.getName();
        this.setter = setter == null ? null : (target, value) -> setValue(setter, target, value);
        this.setterName = setter == null ? name : setter.getName();
        this.ignoreNull = ignoreNull;
    }

    public <F, V> EntryMeta(String name, EntryType type, String getterName, Function<F, V> getter, String setterName, BiConsumer<F, V> setter, boolean ignoreNull) {
        this.name = name;
        this.type = type;
        this.getterName = getterName;
        this.getter = getter;
        this.setterName = setterName;
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
        return getter == null ? null : getter.apply(target);
    }

    /**
     * 设置字段值
     *
     * @param target Form对象
     * @param value  字段值
     */
    public void set(Object target, Object value) {
        if (setter != null) {
            this.setter.accept(target, value);
        }
    }

    private static Object getValue(Method getter, Object target) {
        if (getter == null) {
            return null;
        }
        try {
            return getter.invoke(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setValue(Method setter, Object target, Object value) {
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