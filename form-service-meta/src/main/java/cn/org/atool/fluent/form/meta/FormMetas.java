package cn.org.atool.fluent.form.meta;

import lombok.ToString;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * 表单项为复合对象时, 表单项的元数据
 *
 * @author darui.wu
 */
@ToString(of = {"entryName", "from"})
public final class FormMetas extends EntryMetas {
    public final boolean isList;

    public final String entryName;

    public final Method setter;

    private final EntryMetas from;

    FormMetas(String entryName, Class objType, Method setter, boolean isList, EntryMetas metas) {
        super(objType);
        this.entryName = entryName;
        this.setter = setter;
        this.isList = isList;
        this.from = metas;
        if (from.isUpdate()) {
            throw new IllegalStateException("Result Form Object can't contain any Update Entry.");
        }
    }

    @Override
    public EntryMeta getCurrPage() {
        return this.from.getCurrPage();
    }

    @Override
    public EntryMeta getPagedTag() {
        return this.from.getPagedTag();
    }

    @Override
    public EntryMeta getPageSize() {
        return this.from.getPageSize();
    }

    @Override
    public Integer getPageSize(Object form) {
        return this.from.getPageSize(form);
    }

    @Override
    public Integer getCurrPage(Object form) {
        return this.from.getCurrPage(form);
    }

    @Override
    public Object getPagedTag(Object form) {
        return this.from.getPagedTag(form);
    }

    @Override
    public List<EntryMeta> allMetas() {
        return this.from.allMetas();
    }

    /**
     * 断开级联关系
     */
    @Override
    public List<FormMetas> getForms() {
        return Collections.emptyList();
    }

    @Override
    public List<EntryMeta> getMetas() {
        return this.from.getMetas();
    }

    @Override
    public List<EntryMeta> getOrderBy() {
        return Collections.emptyList();
    }

    @Override
    public void addMeta(EntryMeta meta) {
        throw new IllegalStateException("can't be accessed by FormMetas.");
    }

    public void setValue(Object target, Object data) {
        try {
            setter.invoke(target, data);
        } catch (Exception e) {
            throw new IllegalStateException("set form value error:" + e.getMessage(), e);
        }
    }
}