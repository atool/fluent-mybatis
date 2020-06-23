package cn.org.atool.fluent.mybatis.demo;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.IUpdate;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;

/**
 * base dao 自定义接口
 *
 * @param <E>
 * @param <Q>
 * @param <U>
 */
public interface MyCustomerInterface<E extends IEntity, Q extends IQuery<E, Q>, U extends IUpdate<E, U, Q>>
    extends IBaseDao<E, Q, U> {
    default Q defaultQuery() {
        return this.query().where().and("is_deleted", EQ, false).end();
    }
}