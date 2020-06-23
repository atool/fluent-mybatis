package cn.org.atool.fluent.mybatis.demo;

import cn.org.atool.fluent.mybatis.interfaces.IBaseDao;
import cn.org.atool.fluent.mybatis.interfaces.IEntity;
import cn.org.atool.fluent.mybatis.interfaces.IQuery;
import cn.org.atool.fluent.mybatis.interfaces.IUpdate;

import static cn.org.atool.fluent.mybatis.condition.model.SqlOp.EQ;

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