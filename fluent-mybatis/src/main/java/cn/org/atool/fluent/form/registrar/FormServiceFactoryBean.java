package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.form.FormKit;
import cn.org.atool.fluent.form.annotation.Action;
import cn.org.atool.fluent.form.annotation.ActionType;
import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.form.meta.EntryMeta;
import cn.org.atool.fluent.form.meta.FormMetas;
import cn.org.atool.fluent.form.validation.ValidKit;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
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
        this.entityClass = this.getEntityClass(api.entity(), api.table());
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
        if (Object.class.equals(method.getDeclaringClass()) || method.isDefault()) {
            return method.invoke(this, args);
        }
        if (args.length == 0) {
            throw new RuntimeException("Method[" + method.getName() + "] of interface[" + this.apiInterface.getName() + "] must be have one parameter.");
        }
        Parameter[] parameters = method.getParameters();
        Action action = method.getDeclaredAnnotation(Action.class);
        Entry entry = parameters[0].getDeclaredAnnotation(Entry.class);
        if (args.length == 1 && entry == null) {
            /* 单个入参, 且非 @Entry 参数场景 */
            assertNotNull("method[" + method.getName() + "] parameter[" + parameters[0].getName() + "]", args[0]);
            ValidKit.validate(args[0]);
            FormMetas metas = FormKit.metas(parameters[0].getType());
            return this.doInvoke(method, action, metas, args[0]);
        } else {
            /* 多个入参, 参数必须设置 @Entry 注解 */
            FormMetas metas = FormKit.metas(method, parameters);
            Map<String, Object> form = this.toMap(parameters, args);
            return this.doInvoke(method, action, metas, form);
        }
    }

    /**
     * 参数转换为map结构
     *
     * @param parameters 参数声明列表
     * @param args       参数值列表
     * @return ignore
     */
    private Map<String, Object> toMap(Parameter[] parameters, Object[] args) {
        Map<String, Object> map = new HashMap<>();
        for (int index = 0; index < parameters.length; index++) {
            Parameter p = parameters[index];
            map.put(p.getName(), args[index]);
        }
        return map;
    }

    /**
     * 执行Form操作
     *
     * @param method api方法
     * @param action 方法执行行为
     * @param metas  入参元数据
     * @param form   入参值
     * @return 执行结果
     */
    private Object doInvoke(Method method, Action action, FormMetas metas, Object form) {
        ActionType mType = action == null ? ActionType.Auto : action.type();
        Class eClass = this.getEntityClass(method.getName(), action);
        if (eClass == null) {
            throw new RuntimeException("Annotation[@ApiMethod] must be declared on method[" + method.getName() + "] of interface[" + this.apiInterface.getName() + "].");
        }
        Class rClass = method.getReturnType();

        if (mType == ActionType.Save) {
            return this.save(eClass, rClass, form, metas);
        } else if (metas.isUpdate()) {
            return FormKit.newUpdate(eClass, form, metas).to().updateBy();
        } else {
            return this.query(eClass, method, form, metas);
        }
    }

    /**
     * 持久化数据
     *
     * @return 持久化后的数据
     */
    private Object save(Class eClass, Class rClass, Object form, FormMetas metas) {
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
    private Object query(Class eClass, Method method, Object form, FormMetas metas) {
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
            FormMetas metas = FormMetas.getFormMeta(rClass);
            for (EntryMeta meta : metas) {
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
    private Class<? extends IEntity> getEntityClass(String mName, Action aMethod) {
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
     * 根据{@link Action}或{@link FormService}注解上声明的entityClass和entityTable
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