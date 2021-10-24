package cn.org.atool.fluent.mybatis.form.meta;

import cn.org.atool.fluent.form.FormItem;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

@SuppressWarnings({"unchecked", "rawtypes"})
@Getter
public class FormMetaList extends ArrayList<FormFieldMeta> {

    public static final KeyMap<FormMetaList> FormMetas = new KeyMap<>();

    public static void addMeta(Class klass, String name, Method method, FormItem fa) {
        if (!FormMetas.containsKey(klass)) {
            FormMetas.put(klass, new FormMetaList());
        }
        FormFieldMeta meta = new FormFieldMeta(name, fa.type(), method, fa.ignoreNull());
        FormMetas.get(klass).add(meta);

    }

    public static Method getMethod(Class klass, Field field) {
        String getter;
        if (field.getType() == boolean.class) {
            getter = "is" + MybatisUtil.capitalFirst(field.getName(), null);
        } else {
            getter = "get" + MybatisUtil.capitalFirst(field.getName(), null);
        }
        try {
            return klass.getMethod(getter);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("getMethod[" + getter + "] err:" + e.getMessage(), e);
        }
    }
}