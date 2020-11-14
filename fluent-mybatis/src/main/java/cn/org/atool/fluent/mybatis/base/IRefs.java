package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.model.Form;
import cn.org.atool.fluent.mybatis.model.FormItem;
import cn.org.atool.fluent.mybatis.model.IFormQuery;
import cn.org.atool.fluent.mybatis.model.IPagedList;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;

/**
 * EntityRefQuery: Entity @RefMethod关联关系, 关联加载基类
 *
 * @author darui.wu
 */
public abstract class IRefs implements ApplicationContextAware, InitializingBean {
    /**
     * 单例变量, 需要被Spring容器初始化时赋值
     */
    private static IRefs INSTANCE;

    /**
     * 返回查询关联单例
     * 必须将子类配置到Spring容器中进行bean初始化
     *
     * @return
     */
    public static IRefs instance() {
        if (INSTANCE == null) {
            throw new RuntimeException("the EntityRefs must be defined as a spring bean.");
        }
        return INSTANCE;
    }

    /**
     * 按条件分页查询
     *
     * @param clazz
     * @param condition
     * @param <E>
     * @return
     */
    public static <E extends IEntity> IPagedList<E> paged(Class<E> clazz, Form condition) {
        assertNotNull("clazz", clazz);
        if (condition.getNextId() != null && condition.getCurrPage() != null) {
            throw new RuntimeException("nextId and currPage can only have one value");
        } else if (condition.getNextId() == null && condition.getCurrPage() == null) {
            throw new RuntimeException("nextId and currPage must have one value");
        } else {
            IQuery<E, ?> query = instance().defaultQuery(clazz);
            WhereBase where = query.where();
            for (FormItem item : condition.getItems()) {
                String column = instance().findColumnByField(clazz, item.getKey());
                if (isBlank(column)) {
                    throw new RuntimeException("the field[" + item.getKey() + "] of Entity[" + clazz.getSimpleName() + "] not found.");
                }
                where.and.apply(column, SqlOp.valueOf(item.getOp()), item.getValue());
            }
            if (condition.getCurrPage() != null) {
                int from = condition.getPageSize() * (condition.getCurrPage() - 1);
                query.limit(from, condition.getPageSize());
                return instance().findMapper(clazz).stdPagedEntity(query);
            } else {
                String column = instance().findPrimaryColumn(clazz);
                where.and.apply(column, SqlOp.GE, condition.getNextId());
                query.limit(condition.getPageSize());
                return instance().findMapper(clazz).tagPagedEntity(query);
            }
        }
    }

    public static <E extends IEntity> IPagedList<E> paged(IFormQuery query) {
        return instance().findMapper(query.entityClass()).stdPagedEntity(query);
    }

    /**
     * 返回clazz实体对应的默认Query实例
     *
     * @param clazz
     * @return
     */
    public abstract IQuery defaultQuery(Class<? extends IEntity> clazz);

    /**
     * 返回clazz实体对应的默认Updater实例
     *
     * @param clazz
     * @return
     */
    public abstract IUpdate defaultUpdater(Class<? extends IEntity> clazz);

    /**
     * 按默认值设置entity属性
     * 默认值值设置见 {@link IDefaultSetter#setInsertDefault(IEntity)}
     *
     * @param clazz  entity类型
     * @param entity
     */
    public abstract void setEntityByDefault(Class clazz, IEntity entity);

    /**
     * entity默认设置器
     *
     * @param clazz
     * @return
     */
    public abstract IDefaultGetter findDefaultGetter(Class clazz);

    /**
     * Form Setter
     *
     * @param setterClass
     * @param query
     * @return
     */
    public abstract FormSetter newFormSetter(Class setterClass, IFormQuery query);

    /**
     * 返回clazz属性field对应的数据库字段名称
     *
     * @param clazz
     * @param field
     * @return
     */
    public abstract String findColumnByField(Class clazz, String field);

    /**
     * 返回clazz实体的主键字段
     *
     * @param clazz
     * @return
     */
    public abstract String findPrimaryColumn(Class clazz);

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
        String methodOfEntity = methodNameOfEntity(methodName, this.findFluentEntityClass(entityClass));
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
        String methodOfEntity = methodNameOfEntity(methodName, this.findFluentEntityClass(entityClass));
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
                return this.invokeRefMethod(methodOfEntity, args);
        }
    }

    /**
     * 反射调用 methodOfEntity 方法
     *
     * @param methodOfEntity 方法名称
     * @param args           方法参数列表
     * @param <T>
     * @return
     */
    private <T> T invokeRefMethod(String methodOfEntity, Object[] args) {
        if (!refMethods.containsKey(methodOfEntity)) {
            throw new RuntimeException("the method[" + methodOfEntity + "] not defined or wrong define.");
        }
        Method method = refMethods.get(methodOfEntity);
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
        List list = this.findMapper(entity).listByMap(where);
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
        this.findMapper(entity).deleteById(entity.findPk());
        return null;
    }

    /**
     * 根据id查找实例
     *
     * @param entity
     * @return
     */
    protected IEntity findEntityById(IEntity entity) {
        IEntity result = this.findMapper(entity).findById(entity.findPk());
        return result;
    }

    /**
     * 按id更新实例
     *
     * @param entity
     * @return
     */
    protected IEntity updateEntityById(IEntity entity) {
        this.findMapper(entity).updateById(entity);
        return entity;
    }

    /**
     * 插入entity
     *
     * @param entity
     * @return
     */
    protected IEntity saveEntity(IEntity entity) {
        this.findDefaultGetter(entity.getClass()).setEntityByDefault(entity);
        this.findMapper(entity).insert(entity);
        return entity;
    }

    private Map<String, Method> refMethods = new ConcurrentHashMap<>(32);

    protected final Map<Class<? extends IEntity>, IDaoMapper> entityMappers = new HashMap<>(16);

    /**
     * 返回标注@FluentMybatis注解Entity类
     *
     * @param clazz
     * @return
     */
    public Class<? extends IEntity> findFluentEntityClass(Class clazz) {
        Class entity = clazz;
        while (!this.entityMappers.containsKey(entity) && entity != Object.class) {
            entity = entity.getSuperclass();
        }
        if (this.entityMappers.containsKey(entity)) {
            return entity;
        } else {
            throw new RuntimeException("the class[" + clazz.getName() + "] is not a @FluentMybatis Entity or it's sub class.");
        }
    }

    public IDaoMapper findMapper(IEntity entity) {
        return this.findMapper(entity.getClass());
    }

    public IDaoMapper findMapper(Class<? extends IEntity> clazz) {
        Class entityClazz = this.findFluentEntityClass(clazz);
        return this.entityMappers.get(entityClazz);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (method.getParameterCount() == 0) {
                continue;
            }
            Class parameterType = method.getParameterTypes()[0];
            if (RichEntity.class.isAssignableFrom(parameterType)) {
                this.refMethods.put(method.getName(), method);
            }
        }
        this.initEntityMapper();
        INSTANCE = this;
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