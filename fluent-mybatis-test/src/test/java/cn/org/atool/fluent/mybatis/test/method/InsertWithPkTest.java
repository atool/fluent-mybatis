package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InsertWithPkTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void testInsertWithPk() {
        ATM.dataMap.student.cleanTable();
        StudentEntity student = new StudentEntity()
            .setId(34L)
            .setUserName("user name")
            .setAge(25);
        mapper.insertWithPk(student);
        ATM.dataMap.student.table(1)
            .id.values(34L)
            .userName.values("user name")
            .age.values(25)
            .eqTable();
    }
}
