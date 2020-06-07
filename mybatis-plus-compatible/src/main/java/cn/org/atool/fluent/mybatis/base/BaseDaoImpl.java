package cn.org.atool.fluent.mybatis.base;

/**
 * @param <E>
 * @author darui.wu
 */
@Deprecated
public abstract class BaseDaoImpl<E extends IEntity, Q extends IEntityQuery<Q, E>, U extends IEntityUpdate<U>>
    extends cn.org.atool.fluent.mybatis.condition.base.BaseDaoImpl<E, Q, U> {
}