package cn.org.atool.fluent.mybatis.test.autoid;

import cn.org.atool.fluent.mybatis.generator.shared2.entity.HomeAddressEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UpdateById_WithDefault extends BaseTest {
    @DisplayName("updateById默认设置验证")
    @Test
    void updateById() {
        new HomeAddressEntity().setId(234L)
            .setCity("city")
            .setProvince("tst")
            .updateById();
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE `home_address` " +
            "SET `address` = ?, `city` = ?, `province` = ?, `gmt_modified` = now() " +
            "WHERE `id` = ?");
    }
}
