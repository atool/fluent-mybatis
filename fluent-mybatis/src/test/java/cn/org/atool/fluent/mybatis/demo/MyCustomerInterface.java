package cn.org.atool.fluent.mybatis.demo;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IEntityQuery;
import cn.org.atool.fluent.mybatis.base.IEntityUpdate;
import cn.org.atool.fluent.mybatis.base.IMapperDao;

/**
 * base dao 自定义接口
 *
 * @param <E>
 * @param <Q>
 * @param <U>
 */
public interface MyCustomerInterface<E extends IEntity, Q extends IEntityQuery<Q, E>, U extends IEntityUpdate<U>>
        extends IMapperDao<E, Q, U> {
    default Q defaultQuery() {
        return this.query().eq("is_deleted", false);
    }
}