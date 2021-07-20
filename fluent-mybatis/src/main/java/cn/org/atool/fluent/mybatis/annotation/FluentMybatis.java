package cn.org.atool.fluent.mybatis.annotation;

import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.mybatis.metadata.DbType;

import java.lang.annotation.*;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.Entity_Suffix;

/**
 * fluent mybatis相关设置
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FluentMybatis {
    /**
     * 显式指定表名称
     *
     * @return table name
     */
    String table() default "";

    /**
     * 实体类后缀
     *
     * @return suffix of Entity
     */
    String suffix() default Entity_Suffix;

    /**
     * 表名去掉的前缀
     *
     * @return prefix of table
     */
    String prefix() default "";

    /**
     * mapper bean名称前缀
     *
     * @return prefix of Mapper Bean
     */
    String mapperBeanPrefix() default "";

    /**
     * entity, query, updater默认值设置实现
     *
     * @return IDefaultSetter
     */
    Class<? extends IDefaultSetter> defaults() default IDefaultSetter.class;

    /**
     * 自定义的通用Mapper实现
     *
     * @return class of IMapper
     */
    Class<? extends IMapper> superMapper() default IMapper.class;

    /**
     * 分页语法
     *
     * @return type of database
     */
    DbType dbType() default DbType.MYSQL;
}