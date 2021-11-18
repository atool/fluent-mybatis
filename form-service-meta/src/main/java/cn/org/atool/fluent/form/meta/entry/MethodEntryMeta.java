package cn.org.atool.fluent.form.meta.entry;

import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.meta.EntryMeta;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 反射方法构造的EntryMeta
 *
 * @author darui.wu
 */
public class MethodEntryMeta extends EntryMeta {
    private final Method getter;

    private final Method setter;

    public MethodEntryMeta(String name, EntryType type, Method getter, Method setter, boolean ignoreNull) {
        super(name, type, ignoreNull);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    protected Function getter() {
        return target -> {
            if (getter == null || target == null) {
                return null;
            }
            try {
                return getter.invoke(target);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        };
    }

    @Override
    protected BiConsumer setter() {
        return (target, value) -> {
            if (setter == null || target == null) {
                return;
            }
            try {
                setter.invoke(target, value);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        };
    }
}