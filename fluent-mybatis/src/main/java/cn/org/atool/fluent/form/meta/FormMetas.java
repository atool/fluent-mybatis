package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Objects;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.capitalFirst;

/**
 * Form表单对象元数据定义列表
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue"})
@Getter
public class FormMetas extends ArrayList<EntryMeta> {
    private EntryMeta pageSize;

    private EntryMeta currPage;

    private EntryMeta pagedTag;

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

    private EntryMeta addMeta(EntryMeta meta) {
        switch (meta.getType()) {
            case Ignore:
                /* 忽略掉的字段 */
                return meta;
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

    private EntryMeta addMeta(String name, Method getter, Method setter, Entry entry) {
        if (entry == null) {
            return addMeta(new EntryMeta(name, EntryType.EQ, getter, setter, true));
        } else {
            return addMeta(new EntryMeta(name, entry.type(), getter, setter, entry.ignoreNull()));
        }
    }

    /*** ============================ ***/
    public static final KeyMap<cn.org.atool.fluent.form.meta.FormMetas> FormMetas = new KeyMap<>();

    public static cn.org.atool.fluent.form.meta.FormMetas getFormMeta(Class aClass) {
        if (cn.org.atool.fluent.form.meta.FormMetas.FormMetas.containsKey(aClass)) {
            return cn.org.atool.fluent.form.meta.FormMetas.FormMetas.get(aClass);
        }
        synchronized (FormKit.class) {
            if (cn.org.atool.fluent.form.meta.FormMetas.FormMetas.containsKey(aClass)) {
                return cn.org.atool.fluent.form.meta.FormMetas.FormMetas.get(aClass);
            }
            cn.org.atool.fluent.form.meta.FormMetas.FormMetas.put(aClass, new FormMetas());
            Class declared = aClass;
            while (declared != Object.class) {
                try {
                    buildMetasByFormKits(aClass, declared);
                } catch (Exception ignored) {
                    buildMetasByReflector(aClass, declared);
                }
                declared = declared.getSuperclass();
            }
            return cn.org.atool.fluent.form.meta.FormMetas.FormMetas.get(aClass);
        }
    }

    /**
     * 通过{@link cn.org.atool.fluent.form.annotation.Form}注解生成的工具类构造元数据
     *
     * @param aClass 表单类
     */
    private static void buildMetasByFormKits(Class aClass, Class declared) throws Exception {
        if (Objects.equals(declared, Object.class)) {
            return;
        }
        IFormMeta kit = (IFormMeta) Class.forName(declared.getName() + "MetaKit").getDeclaredConstructor().newInstance();
        for (EntryMeta meta : kit.findFormMetas()) {
            addMeta(aClass, meta);
        }
    }

    /**
     * 通过反射方式获取form表单元数据
     *
     * @param aClass 表单类
     */
    private static void buildMetasByReflector(Class aClass, Class declared) {
        if (Objects.equals(declared, Object.class)) {
            return;
        }
        for (Field field : declared.getDeclaredFields()) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isTransient(mod)) {
                continue;
            }
            addFieldMeta(aClass, field);
        }
    }

    private static void addFieldMeta(Class aClass, Field field) {
        Entry entry = field.getAnnotation(Entry.class);
        String name = entry == null || isBlank(entry.value()) ? field.getName() : entry.value();

        Method getter = cn.org.atool.fluent.form.meta.FormMetas.findGetter(aClass, field);
        Method setter = cn.org.atool.fluent.form.meta.FormMetas.findSetter(aClass, field);
        cn.org.atool.fluent.form.meta.FormMetas.addMeta(aClass, name, getter, setter, entry);
    }

    private static void addMeta(Class klass, String name, Method getter, Method setter, Entry entry) {
        if (!FormMetas.containsKey(klass)) {
            FormMetas.put(klass, new FormMetas());
        }
        FormMetas.get(klass).addMeta(name, getter, setter, entry);
    }

    private static void addMeta(Class klass, EntryMeta meta) {
        if (!FormMetas.containsKey(klass)) {
            FormMetas.put(klass, new FormMetas());
        }
        FormMetas.get(klass).addMeta(meta);
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