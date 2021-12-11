package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.EntryType;
import lombok.ToString;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static cn.org.atool.fluent.common.kits.StringKit.wrap;

/**
 * FormFieldMeta: Form字段元数据
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
@ToString(of = "name")
public class EntryMeta implements IEntryMeta {
    /**
     * 字段名称
     */
    public final String name;
    /**
     * 字段java类型
     */
    public final Type javaType;
    /**
     * 类型
     */
    public EntryType entryType;
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

    protected EntryMeta(String name, Type javaType, EntryType entryType, boolean ignoreNull) {
        this.name = name;
        this.javaType = javaType;
        this.entryType = entryType;
        this.ignoreNull = ignoreNull;
        this.getter = this.getter();
        this.setter = this.setter();
    }

    public <F, V> EntryMeta(String name, Type javaType, EntryType entryType, Function<F, V> getter, BiConsumer<F, V> setter, boolean ignoreNull) {
        this.name = name;
        this.javaType = javaType;
        this.entryType = entryType;
        this.getter = getter;
        this.setter = setter;
        this.ignoreNull = ignoreNull;
    }

    protected Function getter() {
        return null;
    }

    protected BiConsumer setter() {
        return null;
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
            throw wrap(e);
        }
    }

    private static void setValue(Method setter, Object target, Object value) {
        if (setter == null) {
            return;
        }
        try {
            setter.invoke(target, value);
        } catch (Exception e) {
            throw wrap(e);
        }
    }
}