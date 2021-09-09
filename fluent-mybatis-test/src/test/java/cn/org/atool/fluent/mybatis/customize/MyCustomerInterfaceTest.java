package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.generate.mapper.HomeAddressMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.HomeAddressUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MyCustomerInterfaceTest extends BaseTest {
    @Autowired
    HomeAddressMapper mapper;

    @Test
    void test() {
        HomeAddressUpdate.updater()
            .set.city().is("ccc").end()
            .where.id().eq(1L).end()
            .of(mapper).updateBy();
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE `home_address` " +
            "SET `gmt_modified` = now(), `address` = ?, `city` = ? " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `id` = ?");
    }
}
