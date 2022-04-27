package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * InsertSelectedTest
 *
 * @author darui.wu
 * @create 2020/5/13 1:19 下午
 */
public class InsertSelectedTest extends BaseTest {
    @Autowired
    private StudentMapper userMapper;

    @Test
    void testInsert() {
        ATM.dataMap.student.cleanTable();
        StudentEntity student = new StudentEntity()
            .setAge(23)
            .setUserName("#{G_commodityCodeSub}");
        userMapper.insert(student);
        ATM.dataMap.student.table(1)
            .age.values(23)
            .userName.values("#{G_commodityCodeSub}")
            .eqTable();
        db.sqlList().wantFirstSql()
            .eq("INSERT INTO fluent_mybatis.student (`age`, `env`, `tenant`, `user_name`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
                "VALUES (?, ?, ?, ?, now(), now(), 0)");
        want.number(student.getId()).isGt(0L);
    }

    @Test
    void testInsert_withId() {
        ATM.dataMap.student.cleanTable();
        StudentEntity student = new StudentEntity()
            .setUserName("${G_commodityCodeSub}")
            .setId(100L)
            .setHomeAddressId(200L);
        userMapper.insertWithPk(student);
        ATM.dataMap.student.table(1)
            .id.values(100)
            .userName.values("${G_commodityCodeSub}")
            .homeAddressId.values(200)
            .eqTable();
        db.sqlList().wantFirstSql()
            .eq("INSERT INTO fluent_mybatis.student (`id`, `env`, `home_address_id`, `tenant`, `user_name`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
                "VALUES (?, ?, ?, ?, ?, now(), now(), 0)");
        want.number(student.getId()).eq(100L);
    }
}
