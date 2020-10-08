package cn.org.atool.fluent.mybatis.generator;

import cn.org.atool.fluent.mybatis.generator.generator.EntityAnnotationGenerator;
import cn.org.atool.fluent.mybatis.generator.generator.EntityApiGenerator;
import org.test4j.generator.mybatis.config.IGlobalConfig;

/**
 * Entity文件生成器
 *
 * @author wudarui
 */
public class EntityGenerator {
    /**
     * 根据class上的主键生成Entity类
     * 根据@Tables上是否设置了 testDir 来判断是否生成配套的test辅助类
     *
     * @param classes
     */
    public static void byAnnotation(Class... classes) {
        for (Class klass : classes) {
            EntityAnnotationGenerator.generate(klass);
        }
    }

    /**
     * 通过java编码设置方式生成Entity
     * 同时生成配套的test辅助类
     *
     * @return
     */
    public static IGlobalConfig buildWithTest() {
        return EntityApiGenerator.buildWithTest();
    }

    /**
     * 通过java编码设置方式生成Entity
     * 不生成配套的test辅助类
     *
     * @return
     */
    public static IGlobalConfig build() {
        return EntityApiGenerator.build();
    }
}
