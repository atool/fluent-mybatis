package cn.org.atool.fluent.mybatis.annotation;

import cn.org.atool.fluent.mybatis.metadata.DbType;

import java.lang.annotation.*;

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
     * @return
     */
    String table() default "";

    /**
     * 实体类后缀
     *
     * @return
     */
    String suffix() default "Entity";

    /**
     * 表名去掉的前缀
     *
     * @return
     */
    String prefix() default "";

    /**
     * mapper bean名称前缀
     *
     * @return
     */
    String mapperBeanPrefix() default "";

    /**
     * 自定义Dao接口
     *
     * @return
     */
    DaoInterface[] daoInterface() default {};

    /**
     * 分页语法
     *
     * @return
     */
    DbType dbType() default DbType.MYSQL;
}