package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.form.FormKit;
import cn.org.atool.fluent.form.IActionAround;
import cn.org.atool.fluent.form.annotation.Action;
import cn.org.atool.fluent.form.annotation.ActionType;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.form.meta.ActionMeta;
import cn.org.atool.fluent.form.meta.FormMetas;
import cn.org.atool.fluent.form.meta.NoActionAround;
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
    private final Class apiInterface;

    private final IActionAround actionAop;

    private final Class entityClass;

    public FormServiceFactoryBean(Class apiInterface, Class aopClass) {
        this.apiInterface = apiInterface;
        FormService api = (FormService) apiInterface.getDeclaredAnnotation(FormService.class);
        this.entityClass = this.getEntityClass(api.entity(), api.table());
        this.actionAop = this.instance(aopClass);
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
        } else {
            Class eClass = this.getEntityClass(method);
            ActionMeta aMeta = this.actionAop.before(eClass, method, args);
            Object result = this.doInvoke(aMeta);
            return this.actionAop.after(eClass, method, result);
        }
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
    private Object doInvoke(ActionMeta action) {
        FormMetas metas = action.metas();
        if (action.actionType == ActionType.Save) {
            return FormKit.save(action, metas);
        } else if (action.actionType == ActionType.Update || metas.isUpdate()) {
            return FormKit.update(action, metas);
        } else {
            return FormKit.query(action, metas);
        }
    }

    /**
     * 返回要操作的表EntityClass
     *
     * @param method 方法
     * @return EntityClass
     */
    private Class<? extends IEntity> getEntityClass(Method method) {
        Action action = method.getDeclaredAnnotation(Action.class);
        if (action == null) {
            return this.entityClass;
        }
        Class entity = this.getEntityClass(action.entityClass(), action.entityTable());
        if (entity == Object.class) {
            entity = this.entityClass;
        }
        if (entity == null || entity == IEntity.class) {
            throw new RuntimeException("The entityClass value of @MethodService of Method[" + method.getName() + "] must be a subclass of IEntity.");
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
        if (If.notBlank(entityTable)) {
            return FormKit.getEntityClass(entityTable);
        } else if (IEntity.class.isAssignableFrom(entityClass)) {
            return entityClass;
        } else if (Object.class.equals(entityClass)) {
            return null;
        } else {
            throw new RuntimeException("The value of entityClass() of @ServiceMethod(@FormService) must be a subclass of IEntity.");
        }
    }

    private static final KeyMap<IActionAround> instances = new KeyMap<IActionAround>().put(NoActionAround.class, NoActionAround.instance);

    private IActionAround instance(Class<? extends IActionAround> aClass) {
        if (instances.containsKey(aClass)) {
            return instances.get(aClass);
        }
        synchronized (instances) {
            if (instances.containsKey(aClass)) {
                return instances.get(aClass);
            }
            IActionAround aop = NoActionAround.instance;
            try {
                aop = aClass.getDeclaredConstructor().newInstance();
            } catch (Exception ignored) {
            }
            instances.put(aClass, aop);
            return aop;
        }
    }
}