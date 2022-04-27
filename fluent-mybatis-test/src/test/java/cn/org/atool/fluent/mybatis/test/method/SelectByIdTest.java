package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.tools.datagen.DataGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectByIdTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_selectById() throws Exception {
        ATM.dataMap.student.initTable(3)
            .userName.values(DataGenerator.increase("username_%d"))
            .cleanAndInsert();
        StudentEntity student = dao.selectById(3L);
        db.sqlList().wantFirstSql()
            .where().eq("`id` = ?");
        want.object(student)
            .eqMap(ATM.dataMap.student.entity()
                .userName.values("username_3")
            );
    }

    @Test
    public void test_selectByIds() throws Exception {
        ATM.dataMap.student.initTable(10)
            .userName.values(DataGenerator.increase("username_%d"))
            .cleanAndInsert();
        List<StudentEntity> users = dao.selectByIds(Arrays.asList(3L, 5L));
        db.sqlList().wantFirstSql()
            .where().eq("`id` IN (?, ?)");
        want.object(users).eqDataMap(ATM.dataMap.student.entity(2)
            .userName.values("username_3", "username_5")
        );
    }

    @Test
    public void test_selectByIds2() throws Exception {
        ATM.dataMap.student.initTable(10)
            .userName.values(DataGenerator.increase("username_%d"))
            .cleanAndInsert();
        List<StudentEntity> users = dao.selectByIds(3L, 5L);
        db.sqlList().wantFirstSql()
            .where().eq("`id` IN (?, ?)");
        want.object(users).eqDataMap(ATM.dataMap.student.entity(2)
            .userName.values("username_3", "username_5")
        );
    }
}
