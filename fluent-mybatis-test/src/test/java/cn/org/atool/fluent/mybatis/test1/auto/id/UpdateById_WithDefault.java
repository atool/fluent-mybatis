package cn.org.atool.fluent.mybatis.test1.auto.id;

import cn.org.atool.fluent.mybatis.generator.shared2.entity.HomeAddressEntity;
import cn.org.atool.fluent.mybatis.test1.BaseTest;
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
            "SET `address` = ?, `gmt_modified` = now(), `city` = ?, `province` = ? " +
            "WHERE `id` = ?");
    }
}
