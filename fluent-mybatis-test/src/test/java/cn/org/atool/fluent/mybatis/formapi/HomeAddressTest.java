package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.mybatis.formservice.model.HomeAddress;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.dao.intf.HomeAddressDao;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.HomeAddressEntity;
import cn.org.atool.fluent.mybatis.test1.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HomeAddressTest extends BaseTest {
    @Autowired
    HomeAddressDao dao;

    @Test
    void test() {
        ATM.dataMap.homeAddress.table().clean();

        dao.save(HomeAddressEntity.builder()
            .address("oooyyyyxxx")
            .city("123")
            .studentId(0L)
            .district("-=0-9")
            .build());
        HomeAddress address = dao.findHomeAddress("ooo");
        want.object(address).eqReflect(new HomeAddress("oooyyyyxxx", "123", "-=0-9"));
    }

    @Test
    void test2() {
        String toString = dao.toString();
        want.string(toString).start("cn.org.atool.fluent.mybatis.generator.shared2.dao.impl.HomeAddressDaoImpl$$EnhancerByCGLIB$$");

        String result1 = dao.sayImplement();
        want.string(result1).eq("HomeAddressDaoImpl");

        String result2 = dao.sayInterface();
        want.string(result2).eq("HomeAddressDao");
    }

    @Test
    void findByCityAndDistrict() {
        dao.findByCityAndDistrict("hangzhou", "binjiang");
        db.sqlList().wantFirstSql().end("" +
            "FROM `home_address` " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND (`city` = ? AND `district` LIKE ?)");
        db.sqlList().wantFirstPara().eqList(false, "test_env", "hangzhou", "binjiang%");
    }

    @Test
    void findByCityOrDistrictOrAddress() {
        dao.findByCityOrDistrictOrAddress("hangzhou", "binjiang", "hang");
        db.sqlList().wantFirstSql().end("" +
            "FROM `home_address` " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND (`city` = ? OR `district` IN (?) OR `address` LIKE ?)");
        db.sqlList().wantFirstPara().eqList(false, "test_env", "hangzhou", "binjiang", "hang%");
    }
}