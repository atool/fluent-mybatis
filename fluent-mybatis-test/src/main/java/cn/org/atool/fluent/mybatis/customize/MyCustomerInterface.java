package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.base.IDao;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.IUpdate;
import cn.org.atool.fluent.mybatis.base.IWrapper;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;

/**
 * base dao 自定义接口
 *
 * @author wudarui
 */
public interface MyCustomerInterface<E, Q, U> extends IDao<E, Q, U> {

    @Override
    default IWrapper setQueryDefault(Q query) {
        return ((IQuery) query).where()
            .apply("is_deleted", EQ, false)
            .apply("env", EQ, "test_env")
            .end();
    }

    @Override
    default IWrapper setUpdateDefault(U updater) {
        return ((IUpdate) updater)
            .where().apply("is_deleted", EQ, false)
            .apply("env", EQ, "test_env")
            .end();
    }

    default void setInsertDefault(E entity) {
        if (!(entity instanceof IBaseEntity)) {
            return;
        }
        IBaseEntity be = (IBaseEntity) entity;
        be.setEnv("test_env");
        be.setTenant(234567L);
    }
}