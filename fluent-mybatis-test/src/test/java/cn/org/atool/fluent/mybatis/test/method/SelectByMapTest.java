package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.tools.datagen.DataGenerator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author darui.wu 2019/10/29 9:33 下午
 */
public class SelectByMapTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_selectByMap_withStringField() {
        ATM.dataMap.student.initTable(10)
            .userName.values(DataGenerator.increase("username_%d"))
            .env.values("test_env")
            .cleanAndInsert();

        List<StudentEntity> users = dao.selectByMap(new HashMap<String, Object>() {{
            put(Ref.Field.Student.userName.column, "username_4");
        }});
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM fluent_mybatis.student " +
                "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ?");
        want.list(users).eqDataMap(ATM.dataMap.student.entity(1)
            .userName.values("username_4"));
    }

    @Test
    public void test_selectByMap_withNumberField() {
        ATM.dataMap.student.initTable(3)
            .age.values(DataGenerator.increase(20, 1))
            .env.values("test_env")
            .cleanAndInsert();

        List<StudentEntity> students = dao.selectByMap(new HashMap<String, Object>() {{
            put(Ref.Field.Student.age.column, 21);
        }});
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM fluent_mybatis.student " +
                "WHERE `is_deleted` = ? AND `env` = ? AND `age` = ?");
        want.object(students)
            .eqMap(ATM.dataMap.student.entity()
                .age.values(21)
            );
    }

    @Test
    public void test_selectByMap_withDateField() {
        Date date = new Date(1604160000000L);
        ATM.dataMap.student.initTable(1)
            .gmtModified.values(date)
            .env.values("test_env")
            .cleanAndInsert();

        List<StudentEntity> students = dao.selectByMap(new HashMap<String, Object>() {{
            put(Ref.Field.Student.gmtModified.column, date);
        }});
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM fluent_mybatis.student " +
                "WHERE `is_deleted` = ? AND `env` = ? AND `gmt_modified` = ?");
        want.object(students)
            .eqMap(ATM.dataMap.student.entity()
                .gmtModified.values(date)
            );
    }
}
