package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.generator.shared2.entity.HomeAddressEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.HomeAddressUpdate;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;

import java.util.function.Supplier;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;

/**
 * base dao 自定义接口
 *
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
public interface MyCustomerInterface extends IDefaultSetter {

    @Override
    default Supplier<Object> pkGenerator(IEntity entity) {
        if (entity instanceof HomeAddressEntity) {
            return SnowFlakeGenerator::uuid;
        } else {
            return null;
        }
    }

    @Override
    default void setQueryDefault(IQuery query) {
        query.where()
            .apply(F_IS_DELETED, EQ, false)
            .apply(F_ENV, EQ, TEST_ENV)
            .end();
    }

    @Override
    default void setUpdateDefault(IUpdate updater) {
        if (updater instanceof HomeAddressUpdate) {
            updater.updateSet("address", "default address set");
        }
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

    String F_ENV = "env";

    String F_IS_DELETED = "is_deleted";

    String TEST_ENV = "test_env";
}