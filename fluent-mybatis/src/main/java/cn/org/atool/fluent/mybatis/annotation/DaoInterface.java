package cn.org.atool.fluent.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 生成的dao类继承自定义接口
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface DaoInterface {
    /**
     * 接口类
     *
     * @return
     */
    Class value();

    /**
     * 接口泛型参数列表
     *
     * @return
     */
    ParaType[] args() default {};
}