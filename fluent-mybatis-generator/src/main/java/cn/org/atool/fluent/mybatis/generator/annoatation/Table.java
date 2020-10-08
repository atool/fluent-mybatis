package cn.org.atool.fluent.mybatis.generator.annoatation;

import java.lang.annotation.*;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.NOT_DEFINED;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Table {
    /**
     * 表名称列表
     *
     * @return
     */
    String[] value();

    /**
     * 排除字段列表
     *
     * @return
     */
    String[] excludes() default {NOT_DEFINED};

    /**
     * 显式指定字段转换属性
     *
     * @return
     */
    Column[] columns() default {};

    /**
     * 生成Entity文件时, 需要去除的表前缀
     *
     * @return
     */
    String[] tablePrefix() default {NOT_DEFINED};

    /**
     * 生成Mapper bean时在bean name前缀
     *
     * @return
     */
    String mapperPrefix() default NOT_DEFINED;

    /**
     * 记录创建字段
     *
     * @return
     */
    String gmtCreated() default NOT_DEFINED;

    /**
     * 记录修改字段
     *
     * @return
     */
    String gmtModified() default NOT_DEFINED;

    /**
     * 逻辑删除字段
     *
     * @return
     */
    String logicDeleted() default NOT_DEFINED;

    /**
     * 表对应的seq_name
     *
     * @return
     */
    String seqName() default "";

    /**
     * entity类自定义接口
     *
     * @return
     */
    Class[] entityInterface() default {};

    /**
     * dao 类自定义接口
     *
     * @return
     */
    Class[] daoInterface() default {};
}
