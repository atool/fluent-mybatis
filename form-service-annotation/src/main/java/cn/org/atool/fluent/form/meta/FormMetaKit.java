package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.Form;

import java.util.List;

/**
 * 获取{@link Form}注解对象元数据
 *
 * @author wudarui
 */
public interface FormMetaKit {
    /**
     * 返回表单对象元数据
     *
     * @return ignore
     */
    List<EntryMeta> entryMetas();
}