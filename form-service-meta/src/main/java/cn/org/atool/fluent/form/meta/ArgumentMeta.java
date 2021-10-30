package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.FormEntry;
import cn.org.atool.fluent.form.annotation.EntryType;

import java.lang.reflect.Parameter;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 参数定义
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "rawtypes"})
public class ArgumentMeta {
    /**
     * 表单项名称
     */
    public final String entryName;
    /**
     * 参数类型
     */
    public final Class type;
    /**
     * 参数值
     */
    public final Object value;
    /**
     * 表单项类型
     */
    public final EntryType entryType;
    /**
     * 是否忽略空值
     */
    public final boolean ignoreNull;

    public ArgumentMeta(String entryName, EntryType type, Class argType, Object value) {
        this.entryName = entryName;
        this.entryType = type == null ? EntryType.EQ : type;
        this.type = argType;
        this.value = value;
        this.ignoreNull = true;
    }

    public ArgumentMeta(Parameter parameter, Object value) {
        this.value = value;
        this.type = parameter.getType();
        FormEntry entry = parameter.getDeclaredAnnotation(FormEntry.class);
        if (entry == null) {
            this.entryName = null;
            this.entryType = EntryType.Form;
            this.ignoreNull = true;
        } else {
            this.entryName = entry.name();
            this.entryType = entry.type();
            this.ignoreNull = entry.ignoreNull();
        }
    }

    /**
     * 非Form Object对象
     *
     * @return true/false
     */
    public boolean notFormObject() {
        if (type.isPrimitive() || type.getName().startsWith("java.")) {
            /* java自带的类型 */
            return true;
        } else if (Collection.class.isAssignableFrom(type) || type.isArray() || Map.class.isAssignableFrom(type)) {
            /* 数组, 集合, 字典 */
            return true;
        } else { /* 时间 */
            return Date.class.isAssignableFrom(type) || Temporal.class.isAssignableFrom(type);
        }
    }

    /**
     * 参数为表单项
     *
     * @param type 参数类型
     * @param arg  参数值
     * @return ArgumentMeta
     */
    public static ArgumentMeta formArg(Class type, Object arg) {
        return new ArgumentMeta(null, EntryType.Form, type, arg);
    }
}