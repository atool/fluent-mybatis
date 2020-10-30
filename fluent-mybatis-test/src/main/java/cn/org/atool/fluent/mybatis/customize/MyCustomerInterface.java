package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.base.IDao;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;

/**
 * base dao 自定义接口
 *
 * @author wudarui
 */
public interface MyCustomerInterface<E, Q, U> extends IDao<E, Q, U> {

    default Q defaultQuery() {
        return (Q) this.query()
            .where().apply("is_deleted", EQ, false)
            .end();
    }

    default U defaultUpdater() {
        return (U) this.updater()
            .where().apply("is_deleted", EQ, false)
            .end();
    }

    default E setInsertDefault(E entity) {
        return entity;
    }
}