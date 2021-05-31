package cn.org.atool.fluent.mybatis.base.dao;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.dao.intf.StudentDao;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.modes.EqMode;

class IProtectedDaoTest extends BaseTest {
    @Autowired
    private StudentDao dao;

    @Test
    void updateBy() {
        ATM.dataMap.student.initTable(5)
            .id.values(2, 3, 4, 5, 6)
            .address.values("address")
            .age.values(9)
            .cleanAndInsert();
        dao.updateAddressAndAgeById(
            new StudentEntity().setId(3L).setAddress("address 3").setAge(34),
            new StudentEntity().setId(4L).setAddress("address 4").setAge(35),
            new StudentEntity().setId(5L).setAddress("address 5").setAge(36)
        );
        ATM.dataMap.student.table(5)
            .id.values(2, 3, 4, 5, 6)
            .address.values("address", "address 3", "address 4", "address 5", "address")
            .age.values(9, 34, 35, 36, 9)
            .eqTable(EqMode.IGNORE_ORDER);
    }
}
