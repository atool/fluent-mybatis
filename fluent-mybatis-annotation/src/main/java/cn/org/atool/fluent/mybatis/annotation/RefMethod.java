package cn.org.atool.fluent.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 关联方法定义
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface RefMethod {
    /**
     * 自动映射关联关系
     * <p>
     * 如果赋值了, 框架生成代码
     * 赋值格式 "ref.property = this.property &amp;&amp; ref.property = this.property"
     * 注意: 这里是Entity实体字段, 不是数据库表字段
     * <p>
     * 如果未赋值, 则手动实现关联查询
     *
     * @return 关联定义
     */
    String value() default "";
}