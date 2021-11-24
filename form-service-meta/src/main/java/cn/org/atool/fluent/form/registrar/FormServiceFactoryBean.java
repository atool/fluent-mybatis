package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.form.IMethodAround;
import cn.org.atool.fluent.form.annotation.FormMethod;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.form.kits.NoMethodAround;
import cn.org.atool.fluent.form.kits.ParameterizedTypeKit;
import cn.org.atool.fluent.form.meta.MethodArgs;
import cn.org.atool.fluent.form.meta.MethodMeta;
import cn.org.atool.fluent.form.setter.FormHelper;
import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.utility.LockKit;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.springframework.cglib.proxy.Proxy.newProxyInstance;

/**
 * FormServiceFactoryBean: FormService bean封装工厂
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class FormServiceFactoryBean implements FactoryBean {
    public static final KeyMap<Class> TableEntityClass = new KeyMap();

    private final Class serviceClass;

    private final IMethodAround methodAround;

    private final FormService api;

    private Class entityClass;

    public FormServiceFactoryBean(Class serviceClass, Class aroundClass) {
        this.serviceClass = serviceClass;
        this.api = (FormService) serviceClass.getDeclaredAnnotation(FormService.class);
        this.methodAround = this.aroundInstance(aroundClass);
    }

    @Override
    public Class<?> getObjectType() {
        return this.serviceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Object getObject() {
        ClassLoader classLoader = this.serviceClass.getClassLoader();
        return newProxyInstance(classLoader, new Class[]{this.serviceClass}, this::invoke);
    }

    private static final Constructor<MethodHandles.Lookup> constructor;

    static {
        try {
            constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * FactoryBean的 {@link InvocationHandler#invoke(Object, Method, Object[])} 实现
     */
    private Object invoke(Object target, Method method, Object[] args) throws Throwable {
        Class declaringClass = method.getDeclaringClass();
        if (Object.class.equals(declaringClass)) {
            return method.invoke(this, args);
        } else if (method.isDefault()) {
            return constructor.newInstance(declaringClass)
                .in(declaringClass)
                .unreflectSpecial(method, declaringClass)
                .bindTo(target)
                .invokeWithArguments(args);
        } else if (IBaseDao.class.equals(declaringClass) && Objects.equals(method.getName(), "mapper")) {
            Class eClass = this.getEntityClass(method);
            return RefKit.mapper(eClass);
        }
        Class eClass = this.getEntityClass(method);
        try {
            MethodMeta methodMeta = this.methodAround.cache(eClass, method);
            Object[] _args = this.methodAround.before(methodMeta, args);
            Object result = this.doInvoke(methodMeta, _args);
            return this.methodAround.after(eClass, method, args, result);
        } catch (RuntimeException e) {
            return this.methodAround.after(eClass, method, args, e);
        }
    }

    /**
     * 执行Form操作
     *
     * @param args 方法执行行为
     * @return 执行结果
     */
    private Object doInvoke(MethodMeta meta, Object[] args) {
        if (meta.isOneArgList() && !meta.isQuery()) {
            if (meta.isSave()) {
                return save(meta, (Collection) args[0]);
            } else {
                return update(meta, (Collection) args[0]);
            }
        } else {
            if (meta.isSave()) {
                return save(meta, args);
            } else if (meta.isUpdate()) {
                return update(meta, args);
            } else {
                return query(meta, args);
            }
        }
    }

    /**
     * 返回要操作的表EntityClass
     *
     * @param method 方法
     * @return EntityClass
     */
    private Class<? extends IEntity> getEntityClass(Method method) {
        FormMethod aMethod = method.getDeclaredAnnotation(FormMethod.class);
        if (aMethod == null) {
            return this.getEntityClass();
        }
        Class entity = this.getEntityClass(aMethod.entity(), aMethod.table());
        if (entity == null || entity == Object.class) {
            entity = this.getEntityClass();
        }
        if (entity == null || entity == Object.class) {
            throw new RuntimeException("The entityClass value of @MethodService of Method[" + method.getName() + "] must be a subclass of IEntity.");
        }
        return entity;
    }

    private Class<? extends IEntity> getEntityClass() {
        if (this.entityClass == null) {
            this.entityClass = this.getEntityClass(api.entity(), api.table());
        }
        if (entityClass == Object.class && IBaseDao.class.isAssignableFrom(this.serviceClass)) {
            this.entityClass = ParameterizedTypeKit.getType(this.serviceClass, IBaseDao.class, "E");
        }
        return this.entityClass;
    }

    /**
     * 根据{@link FormMethod}或{@link FormService}注解上声明的entityClass和entityTable
     * 值解析实际的EntityClass值
     *
     * @param entityClass Entity类
     * @param entityTable 表名称
     * @return 有效的Entity Class
     */
    private Class getEntityClass(Class entityClass, String entityTable) {
        if (If.notBlank(entityTable)) {
            return this.getEntityClass(entityTable);
        } else if (Object.class.equals(entityClass)) {
            return Object.class;
        } else if (IEntity.class.isAssignableFrom(entityClass)) {
            return entityClass;
        } else {
            throw new RuntimeException("The value of entity() of @Action(@FormService) must be a subclass of IEntity.");
        }
    }

    /**
     * 根据表名称获取实例类型
     *
     * @param table 表名称
     * @return 实例类型
     */
    public Class<? extends IEntity> getEntityClass(String table) {
        if (If.isBlank(table)) {
            return null;
        }
        if (TableEntityClass.containsKey(table)) {
            return TableEntityClass.get(table);
        }
        AMapping mapping = RefKit.byTable(table);
        if (mapping == null) {
            throw new RuntimeException("The table[" + table + "] not found.");
        } else {
            return mapping.entityClass();
        }
    }

    private static final KeyMap<IMethodAround> instances = new KeyMap<IMethodAround>().put(NoMethodAround.class, NoMethodAround.instance);

    private static final LockKit<Class> locks = new LockKit<>(16);

    private IMethodAround aroundInstance(Class<? extends IMethodAround> aClass) {
        locks.lockDoing(instances::containsKey, aClass, () -> {
            try {
                IMethodAround aop = aClass.getDeclaredConstructor().newInstance();
                instances.put(aClass, aop);
            } catch (Exception ignored) {
                instances.put(aClass, NoMethodAround.instance);
            }
        });
        return instances.get(aClass);
    }

    /**
     * 构造eClass实体实例
     *
     * @param meta 操作定义
     * @param args 入参
     * @return entity实例
     */
    public static Object save(MethodMeta meta, Object... args) {
        IEntity entity = FormHelper.newEntity(meta, args);
        Object pk = RefKit.mapper(meta.entityClass).save(entity);
        if (meta.isReturnVoid()) {
            return null;
        } else if (meta.isReturnBool()) {
            return pk != null;
        } else if (meta.returnType.isAssignableFrom(meta.entityClass)) {
            return entity;
        } else {
            return FormHelper.entity2result(entity, meta.returnType);
        }
    }

    /**
     * 批量插入
     *
     * @param meta 入参元数据
     * @param list 入参列表
     * @return entity实例
     */
    public static Object save(MethodMeta meta, Collection list) {
        if (list.size() == 0) {
            throw new IllegalArgumentException("the save list can't be empty.");
        }
        List<IEntity> entities = new ArrayList<>();
        for (Object obj : list) {
            IEntity entity = FormHelper.newEntity(meta, new Object[]{obj});
            entities.add(entity);
        }
        int count = RefKit.mapper(meta.entityClass).save(entities);
        return returnBatchResult(meta, count);
    }

    /**
     * 更新操作
     *
     * @param meta 操作定义
     * @param args 入参
     * @return ignore
     */
    public static Object update(MethodMeta meta, Object... args) {
        IUpdate update = FormHelper.newUpdate(new MethodArgs(meta, args));
        int count = RefKit.mapper(meta.entityClass).updateBy(update);
        return returnBatchResult(meta, count);
    }

    /**
     * 更新操作
     *
     * @param meta 方法元数据
     * @param list 入参是List
     * @return ignore
     */
    public static Object update(MethodMeta meta, Collection list) {
        if (list.size() == 0) {
            throw new IllegalArgumentException("the update list can't be empty.");
        }
        IUpdate[] updates = new IUpdate[list.size()];
        int index = 0;
        for (Object obj : list) {
            IUpdate update = FormHelper.newUpdate(new MethodArgs(meta, new Object[]{obj}));
            updates[index++] = update;
        }
        int count = RefKit.mapper(meta.entityClass).updateBy(updates);
        return returnBatchResult(meta, count);
    }

    /**
     * 构造查询条件实例
     *
     * @param meta 操作定义
     * @param args 入参
     * @return 查询实例
     */
    public static Object query(MethodMeta meta, Object... args) {
        IQuery query = FormHelper.newQuery(new MethodArgs(meta, args));
        if (meta.isCount()) {
            int count = query.to().count();
            return meta.isReturnLong() ? (long) count : count;
        } else if (meta.isStdPage()) {
            /* 标准分页 */
            StdPagedList paged = query.to().stdPagedEntity();
            List data = FormHelper.entities2result(paged.getData(), meta.returnParameterType);
            return paged.setData(data);
        } else if (meta.isTagPage()) {
            /* Tag分页 */
            TagPagedList paged = query.to().tagPagedEntity();
            List data = FormHelper.entities2result(paged.getData(), meta.returnParameterType);
            IEntity next = (IEntity) paged.getNext();
            return new TagPagedList(data, next == null ? null : next.findPk());
        } else if (meta.isList()) {
            /* 返回List */
            List<IEntity> list = query.to().listEntity();
            return FormHelper.entities2result(list, meta.returnParameterType);
        } else {
            /* 查找单条数据 */
            query.limit(1);
            IEntity entity = (IEntity) query.to().findOne().orElse(null);
            return FormHelper.entity2result(entity, meta.returnType);
        }
    }

    private static Object returnBatchResult(MethodMeta meta, int count) {
        if (meta.isReturnVoid()) {
            return null;
        } else if (meta.isReturnBool()) {
            return count > 0;
        } else if (meta.isReturnInt()) {
            return count;
        } else if (meta.isReturnLong()) {
            return (long) count;
        } else {
            throw new IllegalStateException("The type of batch result can only be: void, int, long, or boolean.");
        }
    }
}