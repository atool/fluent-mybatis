package cn.org.atool.fluent.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 标识逻辑删除字段
 *
 * @author wudarui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LogicDelete {
}
