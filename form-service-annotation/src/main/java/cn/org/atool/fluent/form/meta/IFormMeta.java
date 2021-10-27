package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.EntryType;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * IFormMeta
 *
 * @author wudarui
 */
public interface IFormMeta {
    /**
     * 返回表单对象元数据
     *
     * @return ignore
     */
    List<EntryMeta> findFormMetas();

    /**
     * 添加表单元数据
     */
    default <F, V> void add(List<EntryMeta> metas, String name, EntryType type,
                            String getterName, Function<F, V> getter,
                            String setterName, BiConsumer<F, V> setter,
                            boolean ignoreNull) {
        metas.add(new EntryMeta(name, type, getterName, getter, setterName, setter, ignoreNull));
    }
}