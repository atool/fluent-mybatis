package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.form.FormKit;
import cn.org.atool.fluent.form.IPaged;
import cn.org.atool.fluent.form.annotation.ApiMethod;
import cn.org.atool.fluent.form.annotation.FormApi;
import cn.org.atool.fluent.form.meta.FormFieldMeta;
import cn.org.atool.fluent.form.meta.FormMetaList;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Method;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class FormApiFactoryBean implements FactoryBean {

    private final Class apiInterface;

    private final Class entityClass;

    public FormApiFactoryBean(Class apiInterface) {
        this.apiInterface = apiInterface;
        FormApi formApi = (FormApi) apiInterface.getDeclaredAnnotation(FormApi.class);
        this.entityClass = formApi.entity();
    }

    @Override
    public Object getObject() {
        return Proxy.newProxyInstance(this.apiInterface.getClassLoader(), new Class[]{this.apiInterface}, this::invoke);
    }

    public Object invoke(Object target, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        if (args.length != 1) {
            throw new RuntimeException("illegal method: " + method.getName() + " of interface:" + this.apiInterface.getName());
        }
        assertNotNull("parameter of method[" + method.getName() + "]", args[0]);
        ApiMethod apiMethod = method.getDeclaredAnnotation(ApiMethod.class);
        Class eClass = this.getMapping(method.getName(), apiMethod);
        Object form = args[0];
        switch (apiMethod.type()) {
            case Insert:
                return this.save(eClass, method.getReturnType(), form);
            case Update:
                return FormKit.newUpdate(eClass, form).to().updateBy();
            case FindOne:
                return FormKit.newQuery(eClass, form).to().findOne().orElse(null);
            case ListEntity:
            default:
                if (!(form instanceof IPaged)) {
                    return FormKit.newQuery(eClass, form).to().listEntity();
                } else if (((IPaged) form).getCurrPage() == null) {
                    return FormKit.newQuery(eClass, form).to().tagPagedEntity();
                } else {
                    return FormKit.newQuery(eClass, form).to().stdPagedEntity();
                }
        }
    }

    private Object save(Class eClass, Class rType, Object form) {
        IEntity entity = FormKit.newEntity(eClass, form);
        RefKit.mapper(eClass).save(entity);
        if (rType == void.class || rType == Void.class) {
            return null;
        } else if (rType.isAssignableFrom(eClass)) {
            return entity;
        }
        try {
            Object target = rType.getDeclaredConstructor().newInstance();
            FormMetaList metas = FormMetaList.getFormMeta(rType);
            for (FormFieldMeta meta : metas) {
                Object value = entity.valueByField(meta.getName());
                meta.set(target, value);
            }
            return target;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Class getMapping(String method, ApiMethod apiMethod) {
        if (apiMethod == null) {
            throw new RuntimeException("illegal method: " + method + " of interface:" + this.apiInterface.getName());
        }
        Class entity = apiMethod.entity();
        if (entity == null) {
            entity = this.entityClass;
        }
        if (entity == null || entity == IEntity.class) {
            throw new RuntimeException("illegal method: " + method + " of interface:" + this.apiInterface.getName());
        }
        return entity;
    }

    @Override
    public Class<?> getObjectType() {
        return this.apiInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}