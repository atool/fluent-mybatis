package cn.org.atool.fluent.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 标识记录创建时间字段
 *
 * @author wudarui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GmtCreate {
}
