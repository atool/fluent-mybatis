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
        db.table(ATM.Table.student).clean();
        StudentEntity student = new StudentEntity()
            .setAge(23)
            .setUserName("tom mike");
        userMapper.insert(student);
        db.table(ATM.Table.student).query().eqDataMap(ATM.DataMap.student.table(1)
            .age.values(23)
            .userName.values("tom mike")
        );
        db.sqlList().wantFirstSql()
            .eq("INSERT INTO student(gmt_created, gmt_modified, is_deleted, age, env, tenant, user_name) " +
                "VALUES (now(), now(), 0, ?, ?, ?, ?)");
        want.number(student.getId()).isGt(0L);
    }

    @Test
    void testInsert_withId() {
        db.table(ATM.Table.student).clean();
        StudentEntity student = new StudentEntity()
            .setUserName("tom mike")
            .setId(100L)
            .setHomeAddressId(200L);
        userMapper.insert(student);
        db.table(ATM.Table.student).query().eqDataMap(ATM.DataMap.student.table(1)
            .id.values(100)
            .userName.values("tom mike")
            .homeAddressId.values(200)
        );
        db.sqlList().wantFirstSql()
            .eq("INSERT INTO student(id, gmt_created, gmt_modified, is_deleted, env, home_address_id, tenant, user_name) " +
                "VALUES (?, now(), now(), 0, ?, ?, ?, ?)");
        want.number(student.getId()).eq(100L);
    }
}