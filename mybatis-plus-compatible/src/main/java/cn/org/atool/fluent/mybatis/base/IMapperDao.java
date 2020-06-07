package cn.org.atool.fluent.mybatis.base;

/**
 * IMapperDao
 *
 * @author:darui.wu Created by darui.wu on 2019/10/29.
 */
@Deprecated
public interface IMapperDao<E extends IEntity, Q extends IEntityQuery<Q, E>, U extends IEntityUpdate<U>>
    extends cn.org.atool.fluent.mybatis.condition.interfaces.IMapperDao<E, Q, U> {
}