package cn.org.atool.fluent.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 非数据库字段
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotField {
}