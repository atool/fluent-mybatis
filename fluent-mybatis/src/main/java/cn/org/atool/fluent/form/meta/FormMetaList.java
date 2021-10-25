package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.form.FormKit;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.capitalFirst;

@SuppressWarnings({"unchecked", "rawtypes"})
@Getter
public class FormMetaList extends ArrayList<FormFieldMeta> {

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
        FormFieldMeta meta;
        if (entry == null) {
            meta = new FormFieldMeta(name, EntryType.EQ, getter, setter, true);
        } else {
            meta = new FormFieldMeta(name, entry.type(), getter, setter, entry.ignoreNull());
        }
        FormMetas.get(klass).add(meta);
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