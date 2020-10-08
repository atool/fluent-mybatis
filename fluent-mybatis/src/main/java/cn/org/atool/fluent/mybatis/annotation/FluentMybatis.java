package cn.org.atool.fluent.mybatis.annotation;

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
     * @return
     */
    String table() default "";

    /**
     * 实体类后缀
     *
     * @return
     */
    String suffix() default Entity_Suffix;

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
    Class[] daoInterface() default {};

    /**
     * 分页语法
     *
     * @return
     */
    DbType dbType() default DbType.MYSQL;
}