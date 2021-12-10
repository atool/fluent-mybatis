package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.Form;
import cn.org.atool.fluent.form.kits.EntryMetaKit;
import cn.org.atool.fluent.form.kits.ParameterizedTypeKit;
import cn.org.atool.fluent.form.meta.entry.MethodEntryMeta;
import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.BaseEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.utility.LockKit;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.org.atool.fluent.mybatis.utility.StrConstant.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.capitalFirst;

/**
 * Form表单对象元数据定义列表
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue"})
@Getter
@ToString(of = "objType")
public class EntryMetas implements IEntryMeta {
    public final Class objType;

    /**
     * 增删改查表单项列表
     */
    private final List<IEntryMeta> metas = new ArrayList<>();
    /**
     * 嵌套表单项 或 关联数据项
     */
    private final List<FormMetas> forms = new ArrayList<>();

    private EntryMeta pageSize;

    private EntryMeta currPage;

    private EntryMeta pagedTag;
    /**
     * 排序列表表单项
     */
    private final List<EntryMeta> orderBy = new ArrayList<>();

    private boolean isUpdate = false;
    /**
     * 关联方式
     */
    private final boolean isAnd;

    public EntryMetas(Class objType, boolean isAnd) {
        this.objType = objType;
        this.isAnd = isAnd;
    }

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
        switch (meta.entryType) {
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
            case OrderBy:
                this.orderBy.add(meta);
                break;
            default:
                this.metas.add(meta);
        }
    }

    public List<IEntryMeta> allMetas() {
        List<IEntryMeta> all = new ArrayList<>();
        all.addAll(this.metas);
        all.addAll(this.orderBy);
        if (this.pageSize != null) {
            all.add(this.pageSize);
        }
        if (this.currPage != null) {
            all.add(this.currPage);
        }
        if (this.pagedTag != null) {
            all.add(this.pagedTag);
        }
        return all;
    }

    private void addMeta(String name, Method getter, Method setter, Entry entry) {
        if (getter != null || setter != null) {
            if (entry == null) {
                this.addMeta(new MethodEntryMeta(name, EntryType.EQ, getter, setter, true));
            } else {
                this.addMeta(new MethodEntryMeta(name, entry.type(), getter, setter, entry.ignoreNull()));
            }
        }
    }

    /*** ============================ ***/
    private static final KeyMap<EntryMetas> ClassFormMetas = new KeyMap<>();


    static List<Class> root_classes = Arrays.asList(Object.class, RichEntity.class, BaseEntity.class);

    /**
     * 按class类进行加锁
     */
    private final static LockKit<Class> ClassLock = new LockKit<>(16);

    /**
     * 获取class的表单元数据
     *
     * @param aClass 表单class
     * @return FormMetas
     */
    public static EntryMetas getFormMeta(Class aClass) {
        ClassLock.lockDoing(ClassFormMetas::containsKey, aClass, () -> ClassFormMetas.put(aClass, buildFormMeta(aClass)));
        return ClassFormMetas.get(aClass);
    }

    private static EntryMetas buildFormMeta(Class aClass) {
        Form form = (Form) aClass.getDeclaredAnnotation(Form.class);
        EntryMetas metas = new EntryMetas(aClass, form == null || form.and());
        /* 放在构造元数据前面, 避免子对象循环构造 */
        ClassFormMetas.put(aClass, metas);
        Class declared = aClass;
        while (!root_classes.contains(declared) && !declared.isPrimitive() && !declared.getName().startsWith("java.")) {
            try {
                metas.addMetasByFormKits(declared);
            } catch (Exception ignored) {
                metas.addMetasByReflector(aClass, declared);
            }
            declared = declared.getSuperclass();
        }
        return metas;
    }

    /**
     * 通过{@link Form}注解生成的工具类构造元数据
     *
     * @param declared 表单类
     */
    private void addMetasByFormKits(Class declared) throws Exception {
        if (Objects.equals(declared, Object.class)) {
            return;
        }
        EntryMetaKit kit = (EntryMetaKit) Class.forName(declared.getName() + "MetaKit").getDeclaredConstructor().newInstance();
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
            String name = this.noEntryName(entry) ? field.getName() : entry.value();
            Class fClass = this.getFieldType(field);

            Method setter = findSetter(aClass, field);
            if (ParameterizedTypeKit.notFormObject(fClass)) {
                Method getter = findGetter(aClass, field);
                if (getter != null || setter != null) {
                    this.addMeta(name, getter, setter, entry);
                }
            } else if (setter != null) {
                EntryMetas form = getFormMeta(fClass);
                boolean isList = List.class.isAssignableFrom(field.getType());
                this.forms.add(new FormMetas(name, fClass, setter, isList, form));
            }
        }
    }

    /**
     * 返回字段类型 或 List的泛型类型
     *
     * @param field 字段定义
     * @return ignore
     */
    private Class getFieldType(Field field) {
        Class fClass = field.getType();
        if (List.class.isAssignableFrom(fClass)) {
            fClass = ParameterizedTypeKit.getType(field.getGenericType(), List.class, "E");
        }
        return fClass == null ? field.getType() : fClass;
    }

    /**
     * 未显式定义表单项名称
     *
     * @param entry 表单项
     * @return true/false
     */
    private boolean noEntryName(Entry entry) {
        return entry == null || If.isBlank(entry.value());
    }

    public static Method findGetter(Class klass, Field field) {
        String getter;
        if (field.getType() == boolean.class) {
            getter = PRE_IS + capitalFirst(field.getName());
        } else {
            getter = PRE_GET + capitalFirst(field.getName());
        }
        try {
            return klass.getMethod(getter);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method findSetter(Class klass, Field field) {
        String setter;
        if (field.getType() == boolean.class && field.getName().startsWith(PRE_IS)) {
            setter = PRE_SET + capitalFirst(field.getName().substring(2));
        } else {
            setter = PRE_SET + capitalFirst(field.getName());
        }
        try {
            return klass.getMethod(setter, field.getType());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}