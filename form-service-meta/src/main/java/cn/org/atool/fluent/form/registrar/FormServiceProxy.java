package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.form.IMethodAround;
import cn.org.atool.fluent.form.annotation.FormMethod;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.form.kits.NoMethodAround;
import cn.org.atool.fluent.form.kits.ParameterizedTypeKit;
import cn.org.atool.fluent.form.meta.MethodMeta;
import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.dao.BaseDao;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.utility.LockKit;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Objects;

import static cn.org.atool.fluent.form.registrar.FormServiceKit.TableEntityClass;

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
        Class declaringClass = method.getDeclaringClass();
        if (IBaseDao.class.equals(declaringClass) && Objects.equals(method.getName(), "mapper")) {
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
                return FormServiceKit.save(meta, (Collection) args[0]);
            } else {
                return FormServiceKit.update(meta, (Collection) args[0]);
            }
        } else {
            if (meta.isSave()) {
                return FormServiceKit.save(meta, args);
            } else if (meta.isUpdate()) {
                return FormServiceKit.update(meta, args);
            } else {
                return FormServiceKit.query(meta, args);
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

    /**
     * 获取类级别的FormService定义的实体类
     *
     * @return IEntity class
     */
    private Class<? extends IEntity> getEntityClass() {
        if (this.entityClass == null) {
            FormService api = (FormService) serviceClass.getDeclaredAnnotation(FormService.class);
            Class klass = this.getEntityClass(api.entity(), api.table());
            if (klass != Object.class) {
                this.entityClass = klass;
            } else if (IBaseDao.class.isAssignableFrom(this.serviceClass)) {
                this.entityClass = ParameterizedTypeKit.getType(this.serviceClass, IBaseDao.class, "E");
            } else {
                this.entityClass = Object.class;
            }
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
}