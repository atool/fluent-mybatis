package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.form.FormKit;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.form.annotation.MethodType;
import cn.org.atool.fluent.form.annotation.ServiceMethod;
import cn.org.atool.fluent.form.meta.FormFieldMeta;
import cn.org.atool.fluent.form.meta.FormMetaList;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.If.notBlank;

/**
 * FormApiFactoryBean: Form API bean封装工厂
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class FormServiceFactoryBean implements FactoryBean {

    private final Class apiInterface;

    private final Class entityClass;

    public FormServiceFactoryBean(Class apiInterface) {
        this.apiInterface = apiInterface;
        FormService api = (FormService) apiInterface.getDeclaredAnnotation(FormService.class);
        this.entityClass = this.getEntityClass(api.entityClass(), api.entityTable());
    }

    @Override
    public Object getObject() {
        return Proxy.newProxyInstance(this.apiInterface.getClassLoader(), new Class[]{this.apiInterface}, this::invoke);
    }

    public Object invoke(Object target, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        ServiceMethod aMethod = this.getApiMethod(method, args);
        Class eClass = this.getMapping(method.getName(), aMethod);

        FormMetaList metas = FormKit.metas(method.getParameterTypes()[0]);
        MethodType mType = aMethod == null ? MethodType.Auto : aMethod.type();
        Class rClass = method.getReturnType();
        if (mType == MethodType.Save) {
            return this.save(eClass, rClass, args[0], metas);
        } else if (metas.isUpdate()) {
            return FormKit.newUpdate(eClass, args[0], metas).to().updateBy();
        } else {
            return this.query(eClass, method, args[0], metas);
        }
    }

    /**
     * 持久化数据
     *
     * @return 持久化后的数据
     */
    private Object save(Class eClass, Class rClass, Object form, FormMetaList metas) {
        IEntity entity = FormKit.newEntity(eClass, form, metas);
        RefKit.mapper(eClass).save(entity);
        if (rClass == void.class || rClass == Void.class) {
            return null;
        } else if (rClass.isAssignableFrom(eClass)) {
            return entity;
        } else {
            return this.entity2result(entity, rClass);
        }
    }

    /**
     * 查找数据(findOne, listEntity, stdPaged, tagPaged)
     *
     * @return 列表数据
     */
    private Object query(Class eClass, Method method, Object form, FormMetaList metas) {
        Class rType = method.getReturnType();
        Class pType = this.getParameterTypeOfReturn(method);
        IQuery query = FormKit.newQuery(eClass, form, metas);
        if (rType == Integer.class || rType == int.class) {
            return query.to().count();
        } else if (rType == Long.class || rType == long.class) {
            return (long) query.to().count();
        } else if (StdPagedList.class.isAssignableFrom(rType)) {
            StdPagedList paged = query.to().stdPagedEntity();
            List data = this.entities2result(paged.getData(), pType);
            return paged.setData(data);
        } else if (TagPagedList.class.isAssignableFrom(rType)) {
            TagPagedList paged = query.to().tagPagedEntity();
            List data = this.entities2result(paged.getData(), pType);
            IEntity next = (IEntity) paged.getNext();
            return new TagPagedList(data, next == null ? null : next.findPk());
        } else if (Collection.class.isAssignableFrom(method.getReturnType())) {
            List<IEntity> list = query.to().listEntity();
            return this.entities2result(list, pType);
        } else {
            /*  查找单条数据 */
            query.limit(1);
            IEntity entity = (IEntity) query.to().findOne().orElse(null);
            return this.entity2result(entity, rType);
        }
    }

    /**
     * 返回方法返回参数的泛型参数类型
     *
     * @param method api方法
     * @return 泛型参数类型
     */
    private Class getParameterTypeOfReturn(Method method) {
        Class pType = null;
        Type rType = method.getGenericReturnType();
        if (rType instanceof ParameterizedType) {
            pType = (Class) ((ParameterizedType) rType).getActualTypeArguments()[0];
        }
        return pType;
    }

    /**
     * 获取方法上声明的注解 @ApiMethod
     *
     * @param method api方法
     * @param args   api方法入参
     * @return @ApiMethod注解实例
     */
    private ServiceMethod getApiMethod(Method method, Object[] args) {
        if (args.length != 1 || args[0] == null) {
            throw new RuntimeException("Method[" + method.getName() + "] of interface[" + this.apiInterface.getName() + "] has one and only one parameter.");
        }
        ServiceMethod serviceMethod = method.getDeclaredAnnotation(ServiceMethod.class);
        if (serviceMethod == null && this.entityClass == null) {
            throw new RuntimeException("Annotation[@ApiMethod] must be declared on method[" + method.getName() + "] of interface[" + this.apiInterface.getName() + "].");
        }
        return serviceMethod;
    }

    private List entities2result(List<IEntity> entities, Class rClass) {
        if (rClass == null) {
            return entities;
        }
        List list = new ArrayList();
        for (IEntity entity : entities) {
            list.add(this.entity2result(entity, rClass));
        }
        return list;
    }

    private Object entity2result(IEntity entity, Class rClass) {
        if (entity == null) {
            return null;
        }
        try {
            Object target = rClass.getDeclaredConstructor().newInstance();

            Map<String, FieldMapping> mapping = RefKit.byEntity(entity.entityClass()).getFieldsMap();
            FormMetaList metas = FormMetaList.getFormMeta(rClass);
            for (FormFieldMeta meta : metas) {
                FieldMapping fm = mapping.get(meta.getName());
                if (fm == null) {
                    throw new RuntimeException("The field[" + meta.getName() + "] of entity[" + entity.entityClass().getName() + "] not found.");
                } else {
                    Object value = fm.getter.get(entity);
                    meta.set(target, value);
                }
            }
            return target;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回要操作的表EntityClass
     *
     * @param mName   方法名称
     * @param aMethod 方法上的ApiMethod注解
     * @return EntityClass
     */
    private Class<? extends IEntity> getMapping(String mName, ServiceMethod aMethod) {
        if (aMethod == null) {
            return this.entityClass;
        }
        Class entity = this.getEntityClass(aMethod.entityClass(), aMethod.entityTable());
        if (entity == Object.class) {
            entity = this.entityClass;
        }
        if (entity == null || entity == IEntity.class) {
            throw new RuntimeException("illegal method: " + mName + " of interface:" + this.apiInterface.getName());
        }
        return entity;
    }

    private Class getEntityClass(Class klass, String table) {
        if (klass != Object.class) {
            if (IEntity.class.isAssignableFrom(klass)) {
                return klass;
            } else {
                throw new RuntimeException("The value of entityClass() must be a subclass of IEntity.");
            }
        } else if (notBlank(table)) {
            return FormKit.getEntityClass(table);
        } else {
            return null;
        }
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