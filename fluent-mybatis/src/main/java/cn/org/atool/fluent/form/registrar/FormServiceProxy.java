package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.common.kits.ParameterizedTypes;
import cn.org.atool.fluent.common.kits.SegmentLocks;
import cn.org.atool.fluent.form.IMethodAround;
import cn.org.atool.fluent.form.annotation.FormMethod;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.form.meta.MethodMeta;
import cn.org.atool.fluent.form.validator.Validation;
import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.dao.BaseDao;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * FormServiceFactoryBean: FormService bean封装工厂
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class FormServiceProxy implements MethodInterceptor {

    private final Class serviceClass;

    private final IMethodAround methodAround;

    private Class entityClass;

    public static Object create(Class serviceClass, Class aroundClass) {
        Enhancer enhancer = new Enhancer();
        if (serviceClass.isInterface()) {
            enhancer.setInterfaces(new Class[]{serviceClass});
        } else {
            enhancer.setSuperclass(serviceClass);
        }
        enhancer.setCallbackFilter(method -> isAbstract(method) ? 0 : 1);
        FormServiceProxy formServiceProxy = new FormServiceProxy(serviceClass, aroundClass);
        enhancer.setCallbacks(new Callback[]{formServiceProxy, NoOp.INSTANCE});
        Object proxy = enhancer.create();
        if (proxy instanceof BaseDao) {
            ((BaseDao) proxy).setMapper(RefKit.mapper(formServiceProxy.getEntityClass()));
        }
        return proxy;
    }

    /**
     * 抽象方法
     *
     * @param method 方法
     * @return true/false
     */
    private static boolean isAbstract(Method method) {
        return Modifier.isAbstract(method.getModifiers());
    }

    public FormServiceProxy(Class serviceClass, Class aroundClass) {
        this.serviceClass = serviceClass;

        this.methodAround = this.aroundInstance(aroundClass);
    }

    /**
     * FactoryBean的 {@link InvocationHandler#invoke(Object, Method, Object[])} 实现
     */
    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) {
        Validation.validate(target, method, args);

        Class declaringClass = method.getDeclaringClass();
        if (IBaseDao.class.equals(declaringClass) && Objects.equals(method.getName(), "mapper")) {
            Class eClass = this.getEntityClass(method);
            return RefKit.mapper(eClass);
        }
        Class eClass = this.getEntityClass(method);
        try {
            MethodMeta methodMeta = this.methodAround.cache(eClass, method);
            Object[] _args = this.methodAround.before(methodMeta, args);
            Object result = FormServiceKit.invoke(methodMeta, _args);
            return this.methodAround.after(eClass, method, args, result);
        } catch (RuntimeException e) {
            return this.methodAround.after(eClass, method, args, e);
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
        Class entity = FormServiceKit.getEntityClass(aMethod.entity(), aMethod.table());
        if (entity == null || entity == Object.class) {
            entity = this.getEntityClass();
        }
        if (entity == null || entity == Object.class) {
            throw new RuntimeException("The entityClass value of @MethodService of Method[" + method.getName() + "] must be a subclass of IEntity.");
        }
        return entity;
    }

    /**
     * 获取类级别的FormService定义的实体类
     *
     * @return IEntity class
     */
    private Class<? extends IEntity> getEntityClass() {
        if (this.entityClass == null) {
            FormService annotation = (FormService) serviceClass.getDeclaredAnnotation(FormService.class);
            Class klass = FormServiceKit.getEntityClass(annotation.entity(), annotation.table());
            if (klass != Object.class) {
                this.entityClass = klass;
            } else if (IBaseDao.class.isAssignableFrom(this.serviceClass)) {
                this.entityClass = ParameterizedTypes.getType(this.serviceClass, IBaseDao.class, "E");
            } else {
                this.entityClass = Object.class;
            }
        }
        return this.entityClass;
    }

    private static final KeyMap<IMethodAround> instances = new KeyMap<IMethodAround>().put(NoMethodAround.class, NoMethodAround.instance);

    private static final SegmentLocks<Class> locks = new SegmentLocks<>(16);

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
}