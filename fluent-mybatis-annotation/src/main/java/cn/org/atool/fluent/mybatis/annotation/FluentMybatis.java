package cn.org.atool.fluent.mybatis.annotation;

import cn.org.atool.fluent.mybatis.metadata.DbType;

import java.lang.annotation.*;

/**
 * fluent mybatis相关设置
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes"})
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
     * 数据库schema
     */
    String schema() default "";

    /**
     * 使用mybatis的二级缓存
     *
     * @return 默认false
     */
    boolean useCached() default false;

    /**
     * 实体类后缀
     *
     * @return suffix of Entity
     */
    String suffix() default "Entity";

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
    String mapperBeanPrefix() default "fm";

    /**
     * entity, query, updater默认值设置实现
     *
     * @return IDefaultSetter
     */
    Class defaults() default Object.class;

    /**
     * 自定义的通用Mapper实现
     *
     * @return class of IMapper
     */
    Class superMapper() default Object.class;

    /**
     * 分页语法
     *
     * @return type of database
     */
    DbType dbType() default DbType.MYSQL;
}