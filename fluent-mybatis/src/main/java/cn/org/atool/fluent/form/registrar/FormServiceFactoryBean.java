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
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static org.springframework.cglib.proxy.Proxy.newProxyInstance;

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
    public Class<?> getObjectType() {
        return this.apiInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Object getObject() {
        ClassLoader classLoader = this.apiInterface.getClassLoader();
        return newProxyInstance(classLoader, new Class[]{this.apiInterface}, this::invoke);
    }

    /**
     * FactoryBean的 {@link InvocationHandler#invoke(Object, Method, Object[])} 实现
     */
    private Object invoke(Object target, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        ServiceMethod aMethod = this.getApiMethod(method, args);
        Class eClass = this.getEntityClass(method.getName(), aMethod);

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
        Object pk = RefKit.mapper(eClass).save(entity);
        if (rClass == void.class || rClass == Void.class) {
            return null;
        } else if (rClass == Boolean.class || rClass == boolean.class) {
            return pk != null;
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
            /* count, 返回int */
            return query.to().count();
        } else if (rType == Long.class || rType == long.class) {
            /* count, 返回long */
            return (long) query.to().count();
        } else if (StdPagedList.class.isAssignableFrom(rType)) {
            /* 标准分页 */
            StdPagedList paged = query.to().stdPagedEntity();
            List data = this.entities2result(paged.getData(), pType);
            return paged.setData(data);
        } else if (TagPagedList.class.isAssignableFrom(rType)) {
            /* Tag分页 */
            TagPagedList paged = query.to().tagPagedEntity();
            List data = this.entities2result(paged.getData(), pType);
            IEntity next = (IEntity) paged.getNext();
            return new TagPagedList(data, next == null ? null : next.findPk());
        } else if (Collection.class.isAssignableFrom(method.getReturnType())) {
            /* 返回List */
            List<IEntity> list = query.to().listEntity();
            return this.entities2result(list, pType);
        } else {
            /* 查找单条数据 */
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
    private Class<? extends IEntity> getEntityClass(String mName, ServiceMethod aMethod) {
        if (aMethod == null) {
            return this.entityClass;
        }
        Class entity = this.getEntityClass(aMethod.entityClass(), aMethod.entityTable());
        if (entity == Object.class) {
            entity = this.entityClass;
        }
        if (entity == null || entity == IEntity.class) {
            throw new RuntimeException("The entityClass value of @MethodService of Method[" + mName + "] must be a subclass of IEntity.");
        }
        return entity;
    }

    /**
     * 根据{@link ServiceMethod}或{@link FormService}注解上声明的entityClass和entityTable
     * 值解析实际的EntityClass值
     *
     * @param entityClass Entity类
     * @param entityTable 表名称
     * @return 有效的Entity Class
     */
    private Class getEntityClass(Class entityClass, String entityTable) {
        if (notBlank(entityTable)) {
            return FormKit.getEntityClass(entityTable);
        } else if (IEntity.class.isAssignableFrom(entityClass)) {
            return entityClass;
        } else if (Object.class.equals(entityClass)) {
            return null;
        } else {
            throw new RuntimeException("The value of entityClass() of @ServiceMethod(@FormService) must be a subclass of IEntity.");
        }
    }
}