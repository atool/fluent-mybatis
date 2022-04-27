package cn.org.atool.fluent.mybatis.test.formservice;

import cn.org.atool.fluent.mybatis.formservice.model.HomeAddress;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.dao.intf.HomeAddressDao;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.HomeAddressEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HomeAddressTest extends BaseTest {
    @Autowired
    HomeAddressDao dao;

    @Test
    void test() {
        ATM.dataMap.homeAddress.cleanTable();

        dao.save(new HomeAddressEntity()
            .setAddress("oooyyyyxxx")
            .setCity("123")
            .setStudentId(0L)
            .setDistrict("-=0-9"));
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

    @Test
    void top3ByCityOrDistrictOrAddressOrderByCityAscDistrictDesc() {
        dao.top3ByCityOrDistrictOrAddressOrderByCityAscDistrictDesc("hangzhou", "binjiang", "hang");
        db.sqlList().wantFirstSql().end("" +
            "FROM `home_address` " +
            "WHERE `is_deleted` = ? AND `env` = ? " +
            "AND (`city` = ? OR `district` IN (?) OR `address` LIKE ?) " +
            "ORDER BY `City` ASC, `District` DESC LIMIT ?, ?");
        db.sqlList().wantFirstPara().eqList(false, "test_env", "hangzhou", "binjiang", "hang%", 0, 3);
    }

    @Test
    void distinctByCityOrderByCityAscDistrict() {
        want.exception(() -> dao.distinctByCityOrderByCityAscDistrict("hangzhou"), IllegalStateException.class
        ).contains("Unable to resolve parameter[index=0] name");
    }

    @Test
    void existsByCity() {
        ATM.dataMap.homeAddress.table()
            .city.values("hz")
            .env.values("test_env")
            .isDeleted.values(false)
            .cleanAndInsert();
        boolean ret = dao.existsByCity("hz");
        want.bool(ret).is(true);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT 1 FROM `home_address` " +
            "WHERE `is_deleted` = ? AND `env` = ? AND (`city` = ?) LIMIT ?, ?");
        ret = dao.existsByCity("nd");
        want.bool(ret).is(false);
    }
}
