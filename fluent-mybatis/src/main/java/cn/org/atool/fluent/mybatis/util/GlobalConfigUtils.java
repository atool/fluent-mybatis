package cn.org.atool.fluent.mybatis.util;

import cn.org.atool.fluent.mybatis.injector.FluentMybatisSqlInjector;
import cn.org.atool.fluent.mybatis.injector.ISqlInjector;
import cn.org.atool.fluent.mybatis.condition.IKeyGenerator;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.MybatisConfiguration;

import java.util.Set;

/**
 * Mybatis全局缓存工具类
 *
 * @author darui.wu
 */
public class GlobalConfigUtils {

    /**
     * 获取默认 MybatisGlobalConfig
     * <p>FIXME 这可能是一个伪装成单例模式的原型模式，暂时不确定</p>
     */
    public static GlobalConfig defaults() {
        return new GlobalConfig().setDbConfig(new GlobalConfig.DbConfig());
    }

    /**
     * 获取MybatisGlobalConfig (统一所有入口)
     *
     * @param configuration Mybatis 容器配置对象
     */
    public static GlobalConfig getGlobalConfig(Configuration configuration) {
        SimpleAssert.notNull(configuration, "Error: You need Initialize MybatisConfiguration !");
        return ((MybatisConfiguration) configuration).getGlobalConfig();
    }

    public static IKeyGenerator getKeyGenerator(Configuration configuration) {
        return getGlobalConfig(configuration).getDbConfig().getKeyGenerator();
    }

    public static ISqlInjector getSqlInjector(Configuration configuration) {
        // fix #140
        GlobalConfig globalConfiguration = getGlobalConfig(configuration);
        ISqlInjector sqlInjector = globalConfiguration.getSqlInjector();
        if (sqlInjector == null) {
            sqlInjector = new FluentMybatisSqlInjector();
            globalConfiguration.setSqlInjector(sqlInjector);
        }
        return sqlInjector;
    }

    public static Class<?> getSuperMapperClass(Configuration configuration) {
        return getGlobalConfig(configuration).getSuperMapperClass();
    }

    public static boolean isSupperMapperChildren(Configuration configuration, Class<?> mapperClass) {
        return getSuperMapperClass(configuration).isAssignableFrom(mapperClass);
    }

    public static Set<String> getMapperRegistryCache(Configuration configuration) {
        return getGlobalConfig(configuration).getMapperRegistryCache();
    }
}