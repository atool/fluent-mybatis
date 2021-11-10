package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.EntryType;
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
public class EntryMeta {
    /**
     * 字段名称
     */
    public final String name;
    /**
     * 类型
     */
    public EntryType type;
    /**
     * getter方法
     */
    public final Function getter;
    /**
     * setter方法
     */
    public final BiConsumer setter;
    /**
     * 忽略空值情况
     */
    public final boolean ignoreNull;

    public EntryMeta(String name, EntryType type, Method getter, Method setter, boolean ignoreNull) {
        this.name = name;
        this.type = type;
        this.getter = getter == null ? null : target -> getValue(getter, target);
        this.setter = setter == null ? null : (target, value) -> setValue(setter, target, value);
        this.ignoreNull = ignoreNull;
    }

    public <F, V> EntryMeta(String name, EntryType type, Function<F, V> getter, boolean ignoreNull) {
        this.name = name;
        this.type = type;
        this.getter = getter;
        this.setter = null;
        this.ignoreNull = ignoreNull;
    }

    public <F, V> EntryMeta(String name, EntryType type, Function<F, V> getter, BiConsumer<F, V> setter, boolean ignoreNull) {
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
     * @param <R>    字段值类型
     * @return 字段值
     */
    public <R> R get(Object target) {
        return getter == null ? null : (R) getter.apply(target);
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