package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.IEntityKit;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.functions.IGetter;
import cn.org.atool.fluent.mybatis.functions.RefFinder;
import cn.org.atool.fluent.mybatis.functions.TableDynamic;
import cn.org.atool.fluent.mybatis.mapper.PrinterMapper;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.typehandler.ConvertorKit;
import cn.org.atool.fluent.mybatis.typehandler.IConvertor;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;
import static java.util.stream.Collectors.toSet;

/**
 * 常用工具方法入口
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
public final class RefKit {
    /**
     * 多对多关联方法实现引用
     */
    public static final KeyMap<IGetter> relations = new KeyMap<>();
    /**
     * EntityClass 和 AMapping关联关系
     */
    public static final KeyMap<AMapping> ENTITY_MAPPING = new KeyMap<>();
    /**
     * EntityClass 和 AMapping关联关系
     */
    public static final KeyMap<AMapping> TABLE_MAPPING = new KeyMap<>();
    /**
     * EntityClass 和 Mapper实例关联关系
     */
    public static final KeyMap<IEntityMapper> ENTITY_MAPPER = new KeyMap<>();
    /**
     * MapperClass 和 AMapping关联关系
     */
    public static final KeyMap<AMapping> MAPPER_MAPPING = new KeyMap<>();

    /**
     * 返回对应实体类的映射关系
     *
     * @param entityClass Entity类类型
     * @return AMapping
     */
    public static AMapping byEntity(String entityClass) {
        if (ENTITY_MAPPING.containsKey(entityClass)) {
            return ENTITY_MAPPING.get(entityClass);
        }
        throw new RuntimeException("the class[" + entityClass + "] is not a @FluentMybatis Entity or it's Mapper not defined as bean.");
    }

    /**
     * 返回对应表的映射关系
     *
     * @param table 数据库表
     * @return AMapping
     */
    public static AMapping byTable(String table) {
        if (TABLE_MAPPING.containsKey(table)) {
            return TABLE_MAPPING.get(table);
        }
        throw new RuntimeException("the entity of table[" + table + "] is not a @FluentMybatis Entity or it's Mapper not defined as bean.");
    }

    /**
     * 返回对应实体类的映射关系
     *
     * @param clazz Entity类类型
     * @return AMapping
     */
    public static AMapping byEntity(Class clazz) {
        return byEntity(clazz.getName());
    }

    public static IEntityKit entityKit(Class clazz) {
        return byEntity(clazz.getName());
    }

    /**
     * 返回对应Mapper类的映射关系
     *
     * @param mapperClass Mapper类类型
     * @return AMapping
     */
    public static AMapping byMapper(String mapperClass) {
        if (MAPPER_MAPPING.containsKey(mapperClass)) {
            return MAPPER_MAPPING.get(mapperClass);
        }
        throw notFluentMybatisMapper(mapperClass);
    }

    /**
     * 返回对应Mapper类的映射关系
     *
     * @param clazz Mapper类类型
     * @return AMapping
     */
    public static IMapping byMapper(Class clazz) {
        return byMapper(clazz.getName());
    }

    public static <M extends IEntityMapper> M mapperByEntity(Class entityClass) {
        return mapperByEntity(entityClass.getName());
    }

    public static <M extends IEntityMapper> M mapperByEntity(String entityClass) {
        if (ENTITY_MAPPER.containsKey(entityClass)) {
            return (M) ENTITY_MAPPER.get(entityClass);
        }
        throw notFluentMybatisEntity(entityClass);
    }


    /**
     * 返回clazz属性field对应的数据库字段名称
     *
     * @param eClass Entity类类型
     * @param field  entity属性名
     * @return 数据库字段名称
     */
    public static <E extends IEntity> String columnOfField(Class<E> eClass, String field) {
        IMapping mapping = RefKit.byEntity(eClass);
        if (mapping == null) {
            throw notFluentMybatisEntity(eClass);
        } else {
            return mapping.columnOfField(field);
        }
    }

    /**
     * 返回clazz实体的主键字段
     *
     * @param eClass Entity类类型
     * @return 主键字段
     */
    public static <E extends IEntity> String primaryId(Class<E> eClass) {
        IMapping mapping = RefKit.byEntity(eClass);
        if (mapping == null) {
            throw notFluentMybatisEntity(eClass);
        } else {
            return mapping.primaryId(false);
        }
    }

    /**
     * 所有Entity Class
     *
     * @return ignore
     */
    public static Set<String> allEntityClass() {
        return ENTITY_MAPPER.keySet();
    }

    /**
     * 设置对应的实体类对应的数据库类型
     *
     * @param dbType   要变更成的数据库类型
     * @param eClasses 如果为空, 变更应用中所有的实体类对应数据库类型; 如果不为空, 变更指定类
     */
    public static void dbType(DbType dbType, Class<? extends IEntity>... eClasses) {
        Set<String> list = RefKit.getEntityClass(eClasses);
        for (String klass : list) {
            RefKit.byEntity(klass).db(dbType);
        }
    }

    /**
     * 设置对应表的命名策略
     *
     * @param tableSupplier 表的命名策略
     * @param eClasses      如果为空, 变更应用中所有的实体类对应数据库类型; 如果不为空, 变更指定类
     */
    public static void tableSupplier(TableDynamic tableSupplier, Class<? extends IEntity>... eClasses) {
        Set<String> list = RefKit.getEntityClass(eClasses);
        for (String klass : list) {
            RefKit.byEntity(klass).setTableSupplier(tableSupplier);
        }
    }

    /**
     * 注册PoJoHelper.toPoJo时特定类型的转换器
     *
     * @param type      类型
     * @param convertor 类型转换器
     */
    public static void register(Type type, IConvertor convertor) {
        ConvertorKit.register(type, convertor);
    }

    /**
     * 注册PoJoHelper.toPoJo时特定类型的转换器
     *
     * @param typeName  类型, 比如 java.util.List&lt;java.lang.String&gt;
     * @param convertor 类型转换器
     */
    public static void register(String typeName, IConvertor convertor) {
        ConvertorKit.register(typeName, convertor);
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
    public static <T> T invoke(Class eClass, String methodName, Object[] args) {
        IEntity entity = (IEntity) args[0];
        switch (methodName) {
            case RE_Save:
                return save(eClass, entity);
            case RE_UpdateById:
                return updateById(eClass, entity);
            case RE_FindById:
                return findById(eClass, entity);
            case RE_DeleteById:
                return deleteById(eClass, entity);
            case RE_LogicDeleteById:
                return logicDeleteById(eClass, entity);
            case RE_ListByNotNull:
                return listByNotNull(eClass, entity);
            case RE_FirstByNotNull:
                return firstByNotNull(eClass, entity);
            default:
                return invokeRefMethod(eClass, methodName, entity);
        }
    }

    /**
     * 调用RefMethod填充关联数据
     *
     * @param eClass     Entity类
     * @param methodName 关联方法
     * @param eOrList    Entity或Entity列表
     * @return ignore
     */
    public static <T> T invokeRefMethod(Class eClass, String methodName, Object eOrList) {
        String methodOfEntity = methodNameOfEntity(methodName, eClass);
        IGetter finder = relations.get(methodOfEntity);
        if (finder == null) {
            String err = "the method[" + methodName + "] of IEntityRelation not found or IEntityRelation's implement not defined as spring bean.";
            throw new RuntimeException(err);
        } else if (finder instanceof RefFinder) {
            ((RefFinder) finder).relation(eOrList);
            return null;
        } else {
            return (T) finder.get(eOrList);
        }
    }

    private static <T> T listByNotNull(Class eClass, IEntity entity) {
        Map<String, Object> where = entity.toColumnMap();
        assertNotEmpty("the property of entity can't be all empty.", where);
        List list = mapper(eClass).listByMap(true, where);
        return (T) list;
    }

    private static <T> T firstByNotNull(Class eClass, IEntity entity) {
        IQuery query = RefKit.byEntity(eClass).query();
        query.where().eqByEntity(entity).end();
        return (T) query.limit(1).to().findOne().orElse(null);
    }

    private static <T> T logicDeleteById(Class eClass, IEntity entity) {
        mapper(eClass).logicDeleteById(entity.findPk());
        return null;
    }

    private static <T> T deleteById(Class eClass, IEntity entity) {
        mapper(eClass).deleteById(entity.findPk());
        return null;
    }

    private static <T> T findById(Class eClass, IEntity entity) {
        IEntity result = mapper(eClass).findById(entity.findPk());
        return (T) result;
    }

    private static <T> T updateById(Class eClass, IEntity entity) {
        mapper(eClass).updateById(entity);
        return (T) entity;
    }

    private static <T> T save(Class eClass, IEntity entity) {
        mapper(eClass).save(entity);
        return (T) entity;
    }

    /**
     * 返回spring管理对应的mapper bean
     *
     * @param eClass 实体类
     * @return ignore
     */
    public static IRichMapper mapper(Class<? extends IEntity> eClass) {
        eClass = entityClass(eClass);
        IWrapperMapper mapper = mapperByEntity(eClass.getName());
        mapper = PrinterMapper.get(mapper, eClass);
        return mapper;
    }

    public static Set<String> getEntityClass(Class<? extends IEntity>[] eClasses) {
        if (If.isEmpty(eClasses)) {
            return allEntityClass();
        } else {
            return Stream.of(eClasses).map(Class::getName).collect(toSet());
        }
    }

    /**
     * 设置实体类的关联自定义实现
     *
     * @param finder 方法引用
     * @param <E>    实体类型
     */
    public static <E> void put(Class<E> eClass, String refName, IGetter<List<E>> finder) {
        String name = LambdaUtil.resolve(finder);
        relations.put(name, new RefFinder(eClass, refName, finder));
    }

    /**
     * 设置实体类的关联自定义实现
     *
     * @param finder 方法引用
     * @param <E>    实体类型
     */
    public static <E extends IEntity> void put(IGetter<E> finder) {
        String name = LambdaUtil.resolve(finder);
        relations.put(name, finder);
    }

    /**
     * Entity Class不是@FluentMybatis注解类异常
     *
     * @param eClass class
     * @return ignore
     */
    public static FluentMybatisException notFluentMybatisEntity(Class eClass) {
        return new FluentMybatisException("the class[" + eClass.getName() + "] is not a @FluentMybatis Entity or it's sub class.");
    }

    /**
     * Entity Class不是@FluentMybatis注解类异常
     *
     * @param eClass class
     * @return ignore
     */
    public static FluentMybatisException notFluentMybatisEntity(String eClass) {
        return new FluentMybatisException("the class[" + eClass + "] is not a @FluentMybatis Entity or it's sub class.");
    }

    /**
     * Entity Class不是@FluentMybatis注解类异常
     *
     * @param mapperClass class
     * @return ignore
     */
    public static FluentMybatisException notFluentMybatisMapper(String mapperClass) {
        return new FluentMybatisException("the class[" + mapperClass + "] is not a fluent mybatis Mapper bean.");
    }
}