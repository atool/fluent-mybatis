package cn.org.atool.fluent.mybatis.refs;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.IEntityKit;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.functions.RelateFunction;
import cn.org.atool.fluent.mybatis.mapper.PrinterMapper;
import cn.org.atool.fluent.mybatis.utility.LambdaUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;
import static java.util.stream.Collectors.toSet;

/**
 * 框架内部使用方法入口
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
public final class RefKit {
    /**
     * 关联方法实现引用
     */
    public static final KeyMap<RelateFunction<IEntity>> relations = new KeyMap<>();
    /**
     * EntityClass 和 AMapping关联关系
     */
    public static final KeyMap<AMapping> ENTITY_MAPPING = new KeyMap<>();
    /**
     * EntityClass 和 Mapper实例关联关系
     */
    public static final KeyMap<IWrapperMapper> ENTITY_MAPPER = new KeyMap<>();
    /**
     * MapperClass 和 AMapping关联关系
     */
    public static final KeyMap<AMapping> MAPPER_MAPPING = new KeyMap<>();

    /**
     * 返回对应实体类的映射关系
     *
     * @param clazz Entity类类型
     * @return AMapping
     */
    public static AMapping byEntity(String entityClass) {
        if (ENTITY_MAPPING.containsKey(entityClass)) {
            return ENTITY_MAPPING.get(entityClass);
        }
        throw new RuntimeException("the class[" + entityClass + "] is not a @FluentMybatis Entity or it's Mapper not defined as bean.");
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
     * @param clazz Mapper类类型
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

    public static <M extends IWrapperMapper> M mapperByEntity(Class entityClass) {
        return mapperByEntity(entityClass.getName());
    }

    public static <M extends IWrapperMapper> M mapperByEntity(String entityClass) {
        if (ENTITY_MAPPER.containsKey(entityClass)) {
            return (M) ENTITY_MAPPER.get(entityClass);
        }
        throw notFluentMybatisEntity(entityClass);
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
                RelateFunction func = relations.get(methodOfEntity);
                if (func == null) {
                    String err = "the method[" + methodOfEntity + "] not found or IEntityRelation not defined as spring bean.";
                    throw new RuntimeException(err);
                }
                return (T) func.apply(entity);
        }
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
     * @param method 方法引用
     * @param <E>    实体类型
     */
    public static <E extends IEntity> void put(RelateFunction<E> method) {
        String name = LambdaUtil.resolve(method);
        relations.put(name, (RelateFunction) method);
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