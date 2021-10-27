package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.EntryType;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface IFormMeta {
    List<FormFieldMeta> findFormMetas();

    default <F, V> void add(List<FormFieldMeta> metas, String name, EntryType type, String getterName, String setterName, Function<F, V> getter, BiConsumer<F, V> setter, boolean ignoreNull) {
        metas.add(new FormFieldMeta(name, type, getterName, getter, setterName, setter, ignoreNull));
    }
}