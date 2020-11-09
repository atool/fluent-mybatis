package cn.org.atool.fluent.mybatis.base;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotEmpty;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.methodNameOfEntity;

/**
 * EntityRefQuery: Entity @RefMethod关联关系, 关联加载基类
 *
 * @author darui.wu
 */
public abstract class EntityRefQuery implements ApplicationContextAware {
    /**
     * 单例变量, 需要被Spring容器初始化时赋值
     */
    private static EntityRefQuery query;

    /**
     * 返回查询关联单例
     * 必须将子类配置到Spring容器中进行bean初始化
     *
     * @return
     */
    public static EntityRefQuery query() {
        if (query == null) {
            throw new RuntimeException("the LazyFinder must be defined as a spring bean.");
        }
        return query;
    }

    private Map<String, Method> methodsOfService = new ConcurrentHashMap<>(32);

    /**
     * 实现entityClass#methodName方法, 显式指定method的具体实现bean类型: serviceType
     *
     * @param entityClass Entity类类型
     * @param methodName  method name
     * @param serviceType 实现method的bean service
     * @param args        方法入参(第一个参数是entity)
     * @param <T>
     * @return
     */
    public <T> T invoke(Class entityClass, String methodName, Class serviceType, Object[] args) {
        Object bean = this.findBean(serviceType);
        String methodOfEntity = methodNameOfEntity(methodName, this.findFluentMybatisEntity(entityClass));
        if (!methodsOfService.containsKey(methodOfEntity)) {
            this.loadRequiredMethod(methodOfEntity, serviceType, methodName, args);
        }
        try {
            return (T) methodsOfService.get(methodOfEntity).invoke(bean, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查找对应的 serviceType#methodName 方法实现
     *
     * @param methodOfEntity
     * @param serviceType    实现methodName方法的service类型
     * @param methodName     方法名称
     * @param args           方法入参(第一个参数是entity)
     * @return
     */
    private void loadRequiredMethod(String methodOfEntity, Class serviceType, String methodName, Object[] args) {
        synchronized (serviceType) {
            for (Method method : serviceType.getMethods()) {
                if (!Objects.equals(method.getName(), methodName)) {
                    continue;
                }
                if (method.getParameterCount() != args.length) {
                    throw new RuntimeException("Wrong number of method[" + serviceType.getSimpleName() + "#" + methodName + "] parameters");
                }
                methodsOfService.put(methodOfEntity, method);
            }
            throw new RuntimeException("The method[" + serviceType.getSimpleName() + "#" + methodName + "] not found.");
        }
    }

    /**
     * 实现entityClass#methodName方法
     *
     * @param entityClass Entity class name
     * @param methodName  Entity @RefMethod方法
     * @param args        入参(第一个参数是entity)
     * @param <T>
     * @return
     */
    public <T> T invoke(Class entityClass, String methodName, Object[] args) {
        IEntity entity = (IEntity) args[0];
        switch (methodName) {
            case Rich_Entity_Save:
                return (T) this.saveEntity(entity);
            case Rich_Entity_UpdateById:
                return (T) this.updateEntityById(entity);
            case Rich_Entity_FindById:
                return (T) this.findEntityById(entity);
            case Rich_Entity_DeleteById:
                return this.deleteEntityById(entity);
            case RichEntity_ListByNotNull:
                return this.listByEntity(entity);
            default:
                return this.loadRefMethod(methodName, entityClass, args);
        }
    }

    /**
     * 根据methodName和entity获取entity的关联信息
     *
     * @param methodName  方法名称
     * @param entityClass entity类或其子类
     * @param args        方法参数列表
     * @param <T>
     * @return
     */
    private <T> T loadRefMethod(String methodName, Class entityClass, Object[] args) {
        String methodOfEntity = methodNameOfEntity(methodName, this.findFluentMybatisEntity(entityClass));
        if (!methods.containsKey(methodOfEntity)) {
            throw new RuntimeException("the method[" + methodOfEntity + "] not defined or wrong define.");
        }
        Method method = methods.get(methodOfEntity);
        try {
            return (T) method.invoke(this, args);
        } catch (Exception e) {
            String err = String.format("invoke method[%s] error:%s", methodOfEntity, e.getMessage());
            throw new RuntimeException(err, e);
        }
    }

    /**
     * 根据entity非空属性查询表数据
     *
     * @param entity
     * @param <T>
     * @return
     */
    protected <T> T listByEntity(IEntity entity) {
        Map<String, Object> where = entity.toColumnMap();
        assertNotEmpty("where", where);
        List list = this.findEntityMapper(entity).listByMap(where);
        return (T) list;
    }

    /**
     * 根据id物理删除实例
     *
     * @param entity
     * @param <T>
     * @return
     */
    protected <T> T deleteEntityById(IEntity entity) {
        this.findEntityMapper(entity).deleteById(entity.findPk());
        return null;
    }

    /**
     * 根据id查找实例
     *
     * @param entity
     * @return
     */
    protected IEntity findEntityById(IEntity entity) {
        IEntity result = this.findEntityMapper(entity).findById(entity.findPk());
        return result;
    }

    /**
     * 按id更新实例
     *
     * @param entity
     * @return
     */
    protected IEntity updateEntityById(IEntity entity) {
        this.findEntityMapper(entity).updateById(entity);
        return entity;
    }

    /**
     * 插入entity
     *
     * @param entity
     * @return
     */
    protected IEntity saveEntity(IEntity entity) {
        this.findEntityMapper(entity).insert(entity);
        return entity;
    }

    private Map<String, Method> methods = new ConcurrentHashMap<>(32);

    protected final Map<Class<? extends IEntity>, IEntityMapper> entityMappers = new HashMap<>(16);

    /**
     * 返回标注@FluentMybatis注解Entity类
     *
     * @param clazz
     * @return
     */
    public Class<? extends IEntity> findFluentMybatisEntity(Class clazz) {
        Class entity = clazz;
        while (!this.entityMappers.containsKey(entity) && entity != Object.class) {
            entity = clazz.getSuperclass();
        }
        if (this.entityMappers.containsKey(entity)) {
            return entity;
        } else {
            throw new RuntimeException("the class[" + clazz.getSimpleName() + "] is not a @FluentMybatis Entity or it's sub class.");
        }
    }

    private IEntityMapper findEntityMapper(IEntity entity) {
        Class entityClazz = this.findFluentMybatisEntity(entity.getClass());
        return this.entityMappers.get(entityClazz);
    }


    @PostConstruct
    public void init() {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (method.getParameterCount() == 0) {
                continue;
            }
            Class parameterType = method.getParameterTypes()[0];
            if (RichEntity.class.isAssignableFrom(parameterType)) {
                this.methods.put(method.getName(), method);
            }
        }
        this.initEntityMapper();
        query = this;
    }

    protected abstract void initEntityMapper();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Object findBean(Class requiredType) {
        return applicationContext.getBean(requiredType);
    }
}