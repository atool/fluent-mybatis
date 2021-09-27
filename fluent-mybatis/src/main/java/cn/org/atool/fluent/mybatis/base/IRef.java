package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.crud.BaseDefaults;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.IEntityKit;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.mapper.PrinterMapper;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.metadata.SetterMeta;
import cn.org.atool.fluent.mybatis.spring.IConvertor;
import cn.org.atool.fluent.mybatis.spring.MapperFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.Ref_Package;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;
import static java.util.stream.Collectors.toSet;

/**
 * IRef: Entity @RefMethod关联关系, 关联加载基类
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public abstract class IRef {
    /**
     * 单例变量, 需要被Spring容器初始化时赋值
     */
    private static IRef INSTANCE;

    public static IEntityKit entityKit(Class clazz) {
        return (IEntityKit) byEntity(clazz);
    }

    /**
     * 返回查询关联单例
     * 必须将子类配置到Spring容器中进行bean初始化
     *
     * @return IRefs
     */
    public static IRef instance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (IRef.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            try {
                Class klass = Class.forName(Ref_Package + ".AllRef");
                INSTANCE = (IRef) klass.getDeclaredConstructor().newInstance();
                return INSTANCE;
            } catch (Exception e) {
                throw new RuntimeException("new Refs error:" + e.getMessage(), e);
            }
        }
    }

    /**
     * 返回clazz实体对应的默认Query实例
     *
     * @param eClass Entity类类型
     * @return IQuery
     */
    public static IQuery query(Class<? extends IEntity> eClass) {
        return byEntity(entityClass(eClass)).query();
    }

    /**
     * 返回clazz实体对应的空Query实例
     *
     * @param eClass Entity类类型
     * @return IQuery
     */
    public static IQuery emptyQuery(Class<? extends IEntity> eClass) {
        return byEntity(entityClass(eClass)).emptyQuery();
    }

    /**
     * 返回clazz实体对应的默认Updater实例
     *
     * @param eClass Entity类类型
     * @return IUpdate
     */
    public static IUpdate updater(Class<? extends IEntity> eClass) {
        return byEntity(entityClass(eClass)).updater();
    }

    /**
     * 返回clazz实体对应的空Updater实例
     *
     * @param eClass Entity类类型
     * @return IUpdate
     */
    public static IUpdate emptyUpdater(Class<? extends IEntity> eClass) {
        return byEntity(entityClass(eClass)).emptyUpdater();
    }

    /**
     * 返回对应实体类的映射关系
     *
     * @param clazz Entity类类型
     * @return IMapping
     */
    public static IMapping byEntity(Class clazz) {
        return instance().byEntity(clazz.getName());
    }

    protected abstract IMapping byEntity(String clazz);

    /**
     * 返回对应Mapper类的映射关系
     *
     * @param clazz Mapper类类型
     * @return IMapping
     */
    public static IMapping byMapper(Class clazz) {
        return instance().byMapper(clazz.getName());
    }

    protected abstract IMapping byMapper(String clazz);

    /**
     * 返回clazz属性field对应的数据库字段名称
     *
     * @param clazz Entity类类型
     * @param field entity属性名
     * @return 数据库字段名称
     */
    public final String columnOfField(Class clazz, String field) {
        IMapping mapping = byEntity(clazz);
        if (mapping == null) {
            throw notFluentMybatisException(clazz);
        } else {
            return mapping.columnOfField(field);
        }
    }

    /**
     * 返回clazz实体的主键字段
     *
     * @param clazz Entity类类型
     * @return 主键字段
     */
    public String primaryColumn(Class clazz) {
        IMapping mapping = byEntity(clazz);
        if (mapping == null) {
            throw notFluentMybatisException(clazz);
        } else {
            return mapping.primaryId(false);
        }
    }

    /**
     * 实现entityClass#methodName方法
     *
     * @param eClass     Entity class name
     * @param methodName Entity @RefMethod方法
     * @param args       入参(第一个参数是entity)
     * @param <T>        ignore
     * @return ignore
     */
    public <T> T invoke(Class eClass, String methodName, Object[] args) {
        IEntity entity = (IEntity) args[0];
        String methodOfEntity = methodNameOfEntity(methodName, eClass);
        switch (methodName) {
            case RE_Save:
                mapper(eClass).save(entity);
                return (T) entity;
            case RE_UpdateById:
                mapper(eClass).updateById(entity);
                return (T) entity;
            case RE_FindById:
                IEntity result = mapper(eClass).findById(entity.findPk());
                return (T) result;
            case RE_DeleteById:
                mapper(eClass).deleteById(entity.findPk());
                return null;
            case RE_LogicDeleteById:
                mapper(eClass).logicDeleteById(entity.findPk());
                return null;
            case RE_ListByNotNull:
                Map<String, Object> where = entity.toColumnMap();
                assertNotEmpty("the property of entity can't be all empty.", where);
                List list = mapper(eClass).listByMap(true, where);
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
     * 所有Entity Class
     *
     * @return ignore
     */
    protected abstract Set<String> allEntityClass();

    /**
     * 返回所有的Mapper类
     *
     * @return ClassMap
     */
    public abstract KeyMap<AMapping> mapperMapping();

    /**
     * 返回spring管理对应的mapper bean
     *
     * @param eClass 实体类
     * @return ignore
     */
    public static IRichMapper mapper(Class<? extends IEntity> eClass) {
        eClass = entityClass(eClass);
        IWrapperMapper mapper = (IWrapperMapper) instance().mapper(eClass.getName());
        mapper = PrinterMapper.get(mapper, eClass);
        return mapper;
    }

    protected abstract IRichMapper mapper(String eClass);

    /**
     * 从spring容器中获取Mapper
     */
    public void wiredMapper() {
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
    }

    /**
     * 初始化 entity mapper
     */
    protected abstract void initEntityMapper();

    protected MapperFactory mapperFactory;

    protected Object relation;

    /**
     * 设置实体类的关联自定义实现
     *
     * @param relation 实体关联关系实现
     */
    public void setEntityRelation(Object relation, MapperFactory mapperFactory) {
        this.relation = relation;
        this.mapperFactory = mapperFactory;
    }

    /**
     * 根据IEntity类型返回对应的IDefault实例
     *
     * @param clazz IEntity类型
     * @return IDefault
     */
    public BaseDefaults defaults(Class clazz) {
        return (BaseDefaults) byEntity(clazz.getName());
    }

    /**
     * 设置对应的实体类对应的数据库类型
     *
     * @param dbType   要变更成的数据库类型
     * @param entities 如果为空, 变更应用中所有的实体类对应数据库类型; 如果不为空, 变更指定类
     */
    public static void changeDbType(DbType dbType, IEntity... entities) {
        Set<String> list = Stream.of(entities).map(e -> e.getClass().getName()).collect(toSet());
        if (If.isEmpty(entities)) {
            list = instance().allEntityClass();
        }
        for (String klass : list) {
            instance().byEntity(klass).db(dbType);
        }
    }

    public static void register(Type type, IConvertor convertor) {
        SetterMeta.register(type, convertor);
    }

    public static void register(String typeName, IConvertor convertor) {
        SetterMeta.register(typeName, convertor);
    }
}