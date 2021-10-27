package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.form.meta.FormFieldMeta;
import cn.org.atool.fluent.form.meta.IFormMeta;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.form.annotation.EntryType.EQ;
import static cn.org.atool.fluent.form.annotation.EntryType.Update;

@SuppressWarnings("unused")
public class StudentUpdaterMetas implements IFormMeta {
    @Override
    public List<FormFieldMeta> findFormMetas() {
        List<FormFieldMeta> metas = new ArrayList<>();
        this.add(metas, "userName", Update, "getUserName", "setUserName", StudentUpdater::getUserName, StudentUpdater::setUserName, true);
        this.add(metas, "age", Update, "getAge", "setAge", StudentUpdater::getAge, StudentUpdater::setAge, true);
        this.add(metas, "id", EQ, "getId", "setId", StudentUpdater::getId, StudentUpdater::setId, true);
        return metas;
    }
}