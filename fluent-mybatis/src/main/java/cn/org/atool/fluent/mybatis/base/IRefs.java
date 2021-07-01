package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IEntityHelper;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.mapper.EntityHelperFactory;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotEmpty;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.methodNameOfEntity;

/**
 * EntityRefQuery: Entity @RefMethod关联关系, 关联加载基类
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public abstract class IRefs {
    // Ref 文件生成的固定路径
    public static final String Fix_Package = "cn.org.atool.fluent.mybatis.refs";
    /**
     * 单例变量, 需要被Spring容器初始化时赋值
     */
    private static IRefs INSTANCE;

    @Setter
    private DbType defaultDbType;

    /**
     * 返回框架默认的数据库类型
     *
     * @return
     */
    public DbType defaultDbType() {
        if (instance().defaultDbType == null) {
            throw new RuntimeException("please setDefaultDbType(dbType) first.");
        }
        return instance().defaultDbType;
    }

    public static IEntityHelper findEntityHelper(Class clazz) {
        return EntityHelperFactory.getInstance(clazz);
    }

    /**
     * 返回查询关联单例
     * 必须将子类配置到Spring容器中进行bean初始化
     *
     * @return IRefs
     */
    public static IRefs instance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (IRefs.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            try {
                Class klass = Class.forName("cn.org.atool.fluent.mybatis.refs.AllRef");
                INSTANCE = (IRefs) klass.getDeclaredConstructor().newInstance();
                return INSTANCE;
            } catch (Exception e) {
                throw new RuntimeException("new AllRef error:" + e.getMessage(), e);
            }
        }
    }

    protected static RuntimeException springNotInitException() {
        return new RuntimeException("the Refs must be defined as a spring bean.");
    }

    /**
     * 返回clazz实体对应的默认Query实例
     *
     * @param clazz Entity类类型
     * @return IQuery
     */
    public abstract IQuery defaultQuery(Class<? extends IEntity> clazz);

    /**
     * 返回clazz实体对应的空Query实例
     *
     * @param clazz Entity类类型
     * @return IQuery
     */
    public abstract IQuery emptyQuery(Class<? extends IEntity> clazz);

    /**
     * 返回clazz实体对应的默认Updater实例
     *
     * @param clazz Entity类类型
     * @return IUpdate
     */
    public abstract IUpdate defaultUpdater(Class<? extends IEntity> clazz);

    /**
     * 返回clazz实体对应的空Updater实例
     *
     * @param clazz Entity类类型
     * @return IUpdate
     */
    public abstract IUpdate emptyUpdater(Class<? extends IEntity> clazz);

    /**
     * 返回clazz属性field对应的数据库字段名称
     *
     * @param clazz Entity类类型
     * @param field entity属性名
     * @return 数据库字段名称
     */
    public abstract String findColumnByField(Class clazz, String field);

    /**
     * 返回clazz实体的主键字段
     *
     * @param clazz Entity类类型
     * @return 主键字段
     */
    public abstract String findPrimaryColumn(Class clazz);

    /**
     * 实现entityClass#methodName方法
     *
     * @param entityClass Entity class name
     * @param methodName  Entity @RefMethod方法
     * @param args        入参(第一个参数是entity)
     * @param <T>         ignore
     * @return ignore
     */
    public <T> T invoke(Class entityClass, String methodName, Object[] args) {
        IEntity entity = (IEntity) args[0];
        String methodOfEntity = methodNameOfEntity(methodName, this.findFluentEntityClass(entityClass));
        switch (methodName) {
            case Rich_Entity_Save:
                mapper(entity).save(entity);
                return (T) entity;
            case Rich_Entity_UpdateById:
                mapper(entity).updateById(entity);
                return (T) entity;
            case Rich_Entity_FindById:
                IEntity result = mapper(entity).findById(entity.findPk());
                return (T) result;
            case Rich_Entity_DeleteById:
                mapper(entity).deleteById(entity.findPk());
                return null;
            case Rich_Entity_LogicDeleteById:
                mapper(entity).logicDeleteById(entity.findPk());
                return null;
            case RichEntity_ListByNotNull:
                Map<String, Object> where = entity.toColumnMap();
                assertNotEmpty("the property of entity can't be all empty.", where);
                List list = mapper(entity).listByMap(where);
                return (T) list;
            default:
                return this.invokeRefMethod(methodOfEntity, args);
        }
    }

    /**
     * 反射调用 methodOfEntity 方法
     *
     * @param methodOfEntity 方法名称
     * @param args           方法参数列表
     * @param <T>            ignore
     * @return ignore
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

    private final Map<String, Method> refMethods = new ConcurrentHashMap<>(32);

    /**
     * 返回标注@FluentMybatis注解Entity类
     *
     * @param clazz 实例类
     * @return ignore
     */
    public Class<? extends IEntity> findFluentEntityClass(Class clazz) {
        Set<Class<? extends IEntity>> all = this.allEntityClass();
        if (all.isEmpty()) {
            throw new RuntimeException("the sub of IRefs must be a spring bean.");
        }
        Class aClass = clazz;
        while (aClass != Object.class && aClass != RichEntity.class) {
            if (all.contains(aClass)) {
                return aClass;
            } else {
                aClass = aClass.getSuperclass();
            }
        }
        throw new RuntimeException("the class[" + clazz.getName() + "] is not a @FluentMybatis Entity.");
    }

    /**
     * 所有Entity Class
     *
     * @return ignore
     */
    protected abstract Set<Class<? extends IEntity>> allEntityClass();

    /**
     * 返回spring管理对应的mapper bean
     *
     * @param entity 数据库实体类实例
     * @return ignore
     */
    public static IRichMapper mapper(IEntity entity) {
        return IRefs.mapper(entity.getClass());
    }

    /**
     * 返回spring管理对应的mapper bean
     *
     * @param clazz 实体类
     * @return ignore
     */
    public static IRichMapper mapper(Class<? extends IEntity> clazz) {
        return instance().getMapper(clazz);
    }

    protected abstract IRichMapper getMapper(Class<? extends IEntity> clazz);

    /**
     * spring init-method
     */
    @PostConstruct
    public void initMethod() {
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

    /**
     * 初始化 entity mapper
     */
    protected abstract void initEntityMapper();

    @Resource
    protected MapperFactory mapperFactory;
}