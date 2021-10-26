package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.FormKit;
import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.capitalFirst;

/**
 * Form表单对象元数据定义列表
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue"})
@Getter
public class FormMetaList extends ArrayList<FormFieldMeta> {
    private FormFieldMeta pageSize;

    private FormFieldMeta currPage;

    private FormFieldMeta pagedTag;

    private boolean isUpdate = false;

    public Integer getPageSize(Object form) {
        return pageSize == null ? null : (Integer) pageSize.get(form);
    }

    public Integer getCurrPage(Object form) {
        return currPage == null ? null : (Integer) currPage.get(form);
    }

    public Object getPagedTag(Object form) {
        return pagedTag == null ? null : pagedTag.get(form);
    }

    private FormFieldMeta addMeta(String name, Method getter, Method setter, Entry entry) {
        FormFieldMeta meta;
        if (entry == null) {
            meta = new FormFieldMeta(name, EntryType.EQ, getter, setter, true);
        } else if (entry.type() == EntryType.Ignore) {
            /* 忽略掉的字段 */
            return null;
        } else {
            meta = new FormFieldMeta(name, entry.type(), getter, setter, entry.ignoreNull());
        }

        switch (meta.getType()) {
            case PageSize:
                this.pageSize = meta;
                break;
            case CurrPage:
                this.currPage = meta;
                break;
            case PagedTag:
                this.pagedTag = meta;
                break;
            case Update:
                this.isUpdate = true;
                this.add(meta);
                break;
            default:
                this.add(meta);
        }
        return meta;
    }

    /*** ============================ ***/
    public static final KeyMap<FormMetaList> FormMetas = new KeyMap<>();

    public static FormMetaList getFormMeta(Class aClass) {
        if (FormMetaList.FormMetas.containsKey(aClass)) {
            return FormMetaList.FormMetas.get(aClass);
        }
        synchronized (FormKit.class) {
            if (FormMetaList.FormMetas.containsKey(aClass)) {
                return FormMetaList.FormMetas.get(aClass);
            }
            FormMetaList.FormMetas.put(aClass, new FormMetaList());
            Class declared = aClass;
            while (declared != Object.class) {
                for (Field field : declared.getDeclaredFields()) {
                    int mod = field.getModifiers();
                    if (Modifier.isStatic(mod) || Modifier.isTransient(mod)) {
                        continue;
                    }
                    addFieldMeta(aClass, field);
                }
                declared = declared.getSuperclass();
            }
            return FormMetaList.FormMetas.get(aClass);
        }
    }

    private static void addFieldMeta(Class aClass, Field field) {
        Entry entry = field.getAnnotation(Entry.class);
        String name = entry == null || isBlank(entry.value()) ? field.getName() : entry.value();

        Method getter = FormMetaList.findGetter(aClass, field);
        Method setter = FormMetaList.findSetter(aClass, field);
        FormMetaList.addMeta(aClass, name, getter, setter, entry);
    }

    private static void addMeta(Class klass, String name, Method getter, Method setter, Entry entry) {
        if (!FormMetas.containsKey(klass)) {
            FormMetas.put(klass, new FormMetaList());
        }
        FormMetas.get(klass).addMeta(name, getter, setter, entry);
    }

    public static Method findGetter(Class klass, Field field) {
        String getter;
        if (field.getType() == boolean.class) {
            getter = "is" + capitalFirst(field.getName(), null);
        } else {
            getter = "get" + capitalFirst(field.getName(), null);
        }
        try {
            return klass.getMethod(getter);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method findSetter(Class klass, Field field) {
        String setter;
        if (field.getType() == boolean.class && field.getName().startsWith("is")) {
            setter = "set" + capitalFirst(field.getName().substring(2), null);
        } else {
            setter = "set" + capitalFirst(field.getName(), null);
        }
        try {
            return klass.getMethod(setter, field.getType());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}