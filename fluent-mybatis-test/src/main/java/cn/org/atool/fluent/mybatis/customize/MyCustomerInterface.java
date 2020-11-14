package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;

/**
 * base dao 自定义接口
 *
 * @author wudarui
 */
public interface MyCustomerInterface extends IDefaultSetter {
    String F_ENV = "env";

    String F_IS_DELETED = "is_deleted";

    String TEST_ENV = "test_env";

    @Override
    default void setQueryDefault(IQuery query) {
        query.where()
            .apply(F_IS_DELETED, EQ, false)
            .apply(F_ENV, EQ, TEST_ENV)
            .end();
    }

    @Override
    default void setUpdateDefault(IUpdate updater) {
        updater.where()
            .apply(F_IS_DELETED, EQ, false)
            .apply(F_ENV, EQ, TEST_ENV)
            .end();
    }

    @Override
    default void setInsertDefault(IEntity entity) {
        if (!(entity instanceof MyEntity)) {
            return;
        }
        MyEntity be = (MyEntity) entity;
        be.setEnv(TEST_ENV);
        be.setTenant(234567L);
    }
}