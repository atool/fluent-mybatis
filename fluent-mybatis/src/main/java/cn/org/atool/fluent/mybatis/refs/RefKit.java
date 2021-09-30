package cn.org.atool.fluent.mybatis.refs;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseDefaults;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.IEntityKit;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.functions.RelateFunction;
import cn.org.atool.fluent.mybatis.mapper.PrinterMapper;
import cn.org.atool.fluent.mybatis.spring.IMapperFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.refs.ARef.instance;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;
import static java.util.stream.Collectors.toSet;

/**
 * 框架内部使用方法入口
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
public final class RefKit extends IRef {
    /**
     * 初始化 entity mapper 和 关联方法
     */
    public static void initialize(IMapperFactory factory) {
        ARef.instance().initialize(factory);
    }

    public static IEntityKit entityKit(Class clazz) {
        return instance().byEntity(clazz.getName());
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

    /**
     * 返回对应Mapper类的映射关系
     *
     * @param clazz Mapper类类型
     * @return IMapping
     */
    public static IMapping byMapper(Class clazz) {
        return instance().byMapper(clazz.getName());
    }

    /**
     * 根据IEntity类型返回对应的IDefault实例
     *
     * @param clazz IEntity类型
     * @return IDefault
     */
    public static BaseDefaults defaults(Class clazz) {
        return (BaseDefaults) byEntity(clazz);
    }

    /**
     * 返回所有的Mapper类
     *
     * @return ClassMap
     */
    public static KeyMap<AMapping> mapperMapping() {
        return ARef.instance().mapperMapping();
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
                RelateFunction func = instance().relations.get(methodOfEntity);
                if (func == null) {
                    String err = "the method[" + methodOfEntity + "] not defined or wrong define.";
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
        IWrapperMapper mapper = (IWrapperMapper) instance().mapper(eClass.getName());
        mapper = PrinterMapper.get(mapper, eClass);
        return mapper;
    }

    public static Set<String> getEntityClass(Class<? extends IEntity>[] eClasses) {
        if (If.isEmpty(eClasses)) {
            return instance().allEntityClass();
        } else {
            return Stream.of(eClasses).map(Class::getName).collect(toSet());
        }
    }
}