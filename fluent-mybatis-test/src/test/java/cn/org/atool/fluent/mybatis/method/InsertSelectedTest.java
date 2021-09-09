package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
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
        db.table(ATM.table.student).clean();
        StudentEntity student = new StudentEntity()
            .setAge(23)
            .setUserName("#{G_commodityCodeSub}");
        userMapper.insert(student);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(1)
            .age.values(23)
            .userName.values("#{G_commodityCodeSub}")
        );
        db.sqlList().wantFirstSql()
            .eq("INSERT INTO fluent_mybatis.student(`gmt_created`, `gmt_modified`, `is_deleted`, `age`, `env`, `tenant`, `user_name`) " +
                "VALUES (now(), now(), 0, ?, ?, ?, ?)");
        want.number(student.getId()).isGt(0L);
    }

    @Test
    void testInsert_withId() {
        db.table(ATM.table.student).clean();
        StudentEntity student = new StudentEntity()
            .setUserName("${G_commodityCodeSub}")
            .setId(100L)
            .setHomeAddressId(200L);
        userMapper.insertWithPk(student);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(1)
            .id.values(100)
            .userName.values("${G_commodityCodeSub}")
            .homeAddressId.values(200)
        );
        db.sqlList().wantFirstSql()
            .eq("INSERT INTO fluent_mybatis.student(`id`, `gmt_created`, `gmt_modified`, `is_deleted`, `env`, `home_address_id`, `tenant`, `user_name`) " +
                "VALUES (?, now(), now(), 0, ?, ?, ?, ?)");
        want.number(student.getId()).eq(100L);
    }
}
