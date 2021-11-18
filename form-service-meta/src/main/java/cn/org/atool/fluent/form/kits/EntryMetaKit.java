package cn.org.atool.fluent.form.kits;

import cn.org.atool.fluent.form.annotation.Form;
import cn.org.atool.fluent.form.meta.EntryMeta;
import cn.org.atool.fluent.form.processor.MetaKitProcessor;

import java.util.List;

/**
 * 获取{@link Form}注解对象元数据, {@link MetaKitProcessor}编译时生成工具类
 *
 * @author wudarui
 */
public interface EntryMetaKit {
    /**
     * 返回表单对象元数据
     *
     * @return ignore
     */
    List<EntryMeta> entryMetas();
}