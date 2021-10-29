package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Form表单对象元数据定义列表
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue"})
@Getter
public class FormMetas {
    private final List<EntryMeta> metas = new ArrayList<>();

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

    public void addMeta(EntryMeta meta) {
        if (meta == null) {
            return;
        }
        switch (meta.type) {
            case Ignore:
                /* 忽略掉的字段 */
                return;
            case PageSize:
                this.pageSize = meta;
                break;
            case CurrPage:
                this.currPage = meta;
                break;
            case PagedTag:
                this.pagedTag = meta;
                break;
            case Form:
                throw new RuntimeException("can't add EntryType.Form directly.");
            case Update:
                this.isUpdate = true;
                this.metas.add(meta);
                break;
            default:
                this.metas.add(meta);
        }
    }

    private void addMeta(String name, Method getter, Method setter, Entry entry) {
        if (entry == null) {
            this.addMeta(new EntryMeta(name, EntryType.EQ, getter, setter, true));
        } else {
            this.addMeta(new EntryMeta(name, entry.type(), getter, setter, entry.ignoreNull()));
        }
    }

    /*** ============================ ***/
    private static final KeyMap<FormMetas> ClassFormMetas = new KeyMap<>();

    /**
     * 获取class的表单元数据
     *
     * @param aClass 表单class
     * @return FormMetas
     */
    public static FormMetas getFormMeta(Class aClass) {
        if (ClassFormMetas.containsKey(aClass)) {
            return ClassFormMetas.get(aClass);
        }
        synchronized (FormMetas.class) {
            if (ClassFormMetas.containsKey(aClass)) {
                return ClassFormMetas.get(aClass);
            }
            FormMetas metas = new FormMetas();
            Class declared = aClass;
            while (declared != Object.class) {
                try {
                    metas.addMetasByFormKits(declared);
                } catch (Exception ignored) {
                    metas.addMetasByReflector(aClass, declared);
                }
                declared = declared.getSuperclass();
            }
            ClassFormMetas.put(aClass, metas);
            return metas;
        }
    }

    /**
     * 通过{@link cn.org.atool.fluent.form.annotation.Form}注解生成的工具类构造元数据
     *
     * @param declared 表单类
     */
    private void addMetasByFormKits(Class declared) throws Exception {
        if (Objects.equals(declared, Object.class)) {
            return;
        }
        FormMetaKit kit = (FormMetaKit) Class.forName(declared.getName() + "MetaKit").getDeclaredConstructor().newInstance();
        kit.entryMetas().forEach(this::addMeta);
    }

    /**
     * 通过反射方式获取form表单元数据
     *
     * @param aClass 表单类
     */
    private void addMetasByReflector(Class aClass, Class declared) {
        if (Objects.equals(declared, Object.class)) {
            return;
        }
        for (Field field : declared.getDeclaredFields()) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isTransient(mod)) {
                continue;
            }
            Entry entry = field.getAnnotation(Entry.class);
            String name = entry == null || If.isBlank(entry.value()) ? field.getName() : entry.value();

            Method getter = findGetter(aClass, field);
            Method setter = findSetter(aClass, field);
            this.addMeta(name, getter, setter, entry);
        }
    }

    public static Method findGetter(Class klass, Field field) {
        String getter;
        if (field.getType() == boolean.class) {
            getter = "is" + MybatisUtil.capitalFirst(field.getName(), null);
        } else {
            getter = "get" + MybatisUtil.capitalFirst(field.getName(), null);
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
            setter = "set" + MybatisUtil.capitalFirst(field.getName().substring(2), null);
        } else {
            setter = "set" + MybatisUtil.capitalFirst(field.getName(), null);
        }
        try {
            return klass.getMethod(setter, field.getType());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}