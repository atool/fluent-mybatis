package cn.org.atool.fluent.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 支持普通枚举类字段
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnumValue {
}