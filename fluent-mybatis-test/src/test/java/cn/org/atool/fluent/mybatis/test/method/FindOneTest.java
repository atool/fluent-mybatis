package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.dao.intf.StudentDao;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.modes.EqMode;

public class FindOneTest extends BaseTest {
    @Autowired
    StudentDao dao;

    @Test
    void findOne2() {
        ATM.dataMap.student.table().clean();
        // Save操作
        new StudentEntity()
            .setId(2L).setUserName("my.name").setEnv("test_env")
            .setAddress("address").setBonusPoints(34L)
            .save();

        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().eq(2).end();
        StudentEntity student = dao.findOne(query).orElse(null);
        want.object(student).eqReflect(new StudentEntity()
                .setId(2L).setUserName("my.name").setEnv("test_env")
                .setAddress("address").setBonusPoints(34L)
            , EqMode.IGNORE_DEFAULTS);
    }

    @Test
    void saveAndFindByIdAndUpdateById() {
        // 数据准备，清空数据库
        ATM.dataMap.student.table().clean();
        // Save操作
        new StudentEntity()
            .setId(2L).setUserName("my.name").setEnv("test_env")
            .setAddress("address").setBonusPoints(34L)
            .save();
        // findById操作
        StudentEntity student = new StudentEntity().setId(2L).findById();
        // 数据验证
        want.object(student).eqReflect(new StudentEntity()
                .setId(2L).setUserName("my.name").setEnv("test_env")
                .setAddress("address").setBonusPoints(34L)
            , EqMode.IGNORE_DEFAULTS);
        // updateById操作
        new StudentEntity().setId(2L).setUserName("test3").setAddress("address2")
            .updateById();
        // 数据验证
        ATM.dataMap.student.table(1)
            .id.values(2)
            .userName.values("test3")
            .address.values("address2")
            .eqTable();
    }
}
