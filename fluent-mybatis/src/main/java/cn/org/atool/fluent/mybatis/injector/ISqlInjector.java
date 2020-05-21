package cn.org.atool.fluent.mybatis.injector;

import org.apache.ibatis.builder.MapperBuilderAssistant;

/**
 * SQL 自动注入器接口
 *
 * @author darui.wu
 */
public interface ISqlInjector {

    /**
     * 检查SQL是否注入
     *
     * @param builderAssistant mapper 信息
     * @param mapperClass      mapper 接口的 class 对象
     */
    void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass);
}