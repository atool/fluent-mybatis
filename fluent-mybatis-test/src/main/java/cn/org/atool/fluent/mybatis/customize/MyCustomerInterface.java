package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.base.*;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;

/**
 * base dao 自定义接口
 *
 * @author wudarui
 */
public interface MyCustomerInterface extends IDefault {
    String F_ENV = "env";

    String F_IS_DELETED = "is_deleted";

    String TEST_ENV = "test_env";

    @Override
    default IQuery setQueryDefault(IQuery query) {
        return (IQuery) query.where()
            .apply(F_IS_DELETED, EQ, false)
            .apply(F_ENV, EQ, TEST_ENV)
            .end();
    }

    @Override
    default IUpdate setUpdateDefault(IUpdate updater) {
        return (IUpdate) updater
            .where()
            .apply(F_IS_DELETED, EQ, false)
            .apply(F_ENV, EQ, TEST_ENV)
            .end();
    }

    @Override
    default void setInsertDefault(IEntity entity) {
        if (!(entity instanceof IBaseEntity)) {
            return;
        }
        IBaseEntity be = (IBaseEntity) entity;
        be.setEnv(TEST_ENV);
        be.setTenant(234567L);
    }
}