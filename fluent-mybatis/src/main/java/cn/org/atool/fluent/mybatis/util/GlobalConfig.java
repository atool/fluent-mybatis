package cn.org.atool.fluent.mybatis.util;

import cn.org.atool.fluent.mybatis.injector.ISqlInjector;
import cn.org.atool.fluent.mybatis.mapper.IMapper;
import cn.org.atool.fluent.mybatis.annotation.IdType;
import cn.org.atool.fluent.mybatis.condition.IKeyGenerator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Mybatis 全局缓存
 *
 * @author darui.wu
 */
@Data
@Accessors(chain = true)
public class GlobalConfig implements Serializable {

    /**
     * 是否开启 LOGO
     */
    private boolean banner = true;

    /**
     * 数据库相关配置
     */
    private DbConfig dbConfig;
    /**
     * SQL注入器
     */
    private ISqlInjector sqlInjector;
    /**
     * Mapper父类
     */
    private Class<?> superMapperClass = IMapper.class;
    /**
     * 缓存当前Configuration的SqlSessionFactory
     */
    @Setter(value = AccessLevel.NONE)
    private SqlSessionFactory sqlSessionFactory;
    /**
     * 缓存已注入CRUD的Mapper信息
     */
    private Set<String> mapperRegistryCache = new ConcurrentSkipListSet<>();

    /**
     * 标记全局设置 (统一所有入口)
     */
    public void signGlobalConfig(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Data
    public static class DbConfig {
        /**
         * 主键类型（默认 ID_WORKER）
         */
        private IdType idType = IdType.ID_WORKER;
        /**
         * schema
         *
         * @since 3.1.1
         */
        private String schema;

        /**
         * 表名、是否使用下划线命名（默认 true:默认数据库表下划线命名）
         */
        private boolean tableUnderline = true;
        /**
         * 大写命名
         */
        private boolean capitalMode = false;
        /**
         * 表主键生成器
         */
        private IKeyGenerator keyGenerator;
    }
}