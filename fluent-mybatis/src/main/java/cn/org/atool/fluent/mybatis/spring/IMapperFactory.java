package cn.org.atool.fluent.mybatis.spring;

import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;

/**
 * IMapperFactory
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused"})
public interface IMapperFactory {
    /**
     * 返回Mapper实例
     *
     * @param mapperClass IEntityMapper接口类
     * @param <T>         IEntityMapper类型
     * @return IEntityMapper实例
     */
    <T extends IEntityMapper> T getMapper(Class<T> mapperClass);
}