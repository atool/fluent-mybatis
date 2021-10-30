package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.form.FormKit;
import cn.org.atool.fluent.form.IMethodAround;
import cn.org.atool.fluent.form.annotation.FormMethod;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.form.annotation.MethodType;
import cn.org.atool.fluent.form.meta.EntryMetas;
import cn.org.atool.fluent.form.meta.MethodMeta;
import cn.org.atool.fluent.form.meta.NoMethodAround;
import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

import static org.springframework.cglib.proxy.Proxy.newProxyInstance;

/**
 * FormServiceFactoryBean: FormService bean封装工厂
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class FormServiceFactoryBean implements FactoryBean {
    private final Class serviceClass;

    private final IMethodAround methodAround;

    private final Class entityClass;

    public FormServiceFactoryBean(Class serviceClass, Class aroundClass) {
        this.serviceClass = serviceClass;
        FormService api = (FormService) serviceClass.getDeclaredAnnotation(FormService.class);
        this.entityClass = this.getEntityClass(api.entity(), api.table());
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

    /**
     * FactoryBean的 {@link InvocationHandler#invoke(Object, Method, Object[])} 实现
     */
    private Object invoke(Object target, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass()) || method.isDefault()) {
            return method.invoke(this, args);
        } else {
            Class eClass = this.getEntityClass(method);
            MethodMeta aMeta = this.methodAround.before(eClass, method, args);
            Object result = this.doInvoke(aMeta);
            return this.methodAround.after(eClass, method, result);
        }
    }

    /**
     * 执行Form操作
     *
     * @param method 方法执行行为
     * @return 执行结果
     */
    private Object doInvoke(MethodMeta method) {
        EntryMetas metas = method.metas();
        if (method.methodType == MethodType.Save) {
            return FormKit.save(method, metas);
        } else if (method.methodType == MethodType.Update) {
            return FormKit.update(method, metas);
        } else {
            return FormKit.query(method, metas);
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
            return this.entityClass;
        }
        Class entity = this.getEntityClass(aMethod.entity(), aMethod.table());
        if (entity == null) {
            entity = this.entityClass;
        }
        if (entity == null) {
            throw new RuntimeException("The entityClass value of @MethodService of Method[" + method.getName() + "] must be a subclass of IEntity.");
        }
        return entity;
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
            return FormKit.getEntityClass(entityTable);
        } else if (Object.class.equals(entityClass)) {
            return null;
        } else if (IEntity.class.isAssignableFrom(entityClass)) {
            return entityClass;
        } else {
            throw new RuntimeException("The value of entity() of @Action(@FormService) must be a subclass of IEntity.");
        }
    }

    private static final KeyMap<IMethodAround> instances = new KeyMap<IMethodAround>().put(NoMethodAround.class, NoMethodAround.instance);

    private IMethodAround aroundInstance(Class<? extends IMethodAround> aClass) {
        if (instances.containsKey(aClass)) {
            return instances.get(aClass);
        }
        synchronized (instances) {
            if (instances.containsKey(aClass)) {
                return instances.get(aClass);
            }
            IMethodAround aop = NoMethodAround.instance;
            try {
                aop = aClass.getDeclaredConstructor().newInstance();
            } catch (Exception ignored) {
            }
            instances.put(aClass, aop);
            return aop;
        }
    }
}