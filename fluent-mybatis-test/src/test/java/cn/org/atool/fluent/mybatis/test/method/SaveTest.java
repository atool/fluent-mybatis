package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * SaveTest
 *
 * @author darui.wu
 * @create 2019/10/31 11:17 上午
 */
public class SaveTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_save_noPk() throws Exception {
        ATM.dataMap.student.cleanTable();
        StudentEntity entity = new StudentEntity().setUserName("test name").setAge(43);
        dao.save(entity);
        ATM.dataMap.student.table(1)
            .id.values(entity.getId())
            .userName.values("test name")
            .age.values(43)
            .eqTable();
    }

    @Test
    public void test_save_WithPk() throws Exception {
        ATM.dataMap.student.cleanTable();
        dao.save(new StudentEntity().setId(4L).setUserName("test name").setAge(43));
        db.sqlList().wantFirstSql().contains("`id`,");
        ATM.dataMap.student.table(1)
            .id.values(4)
            .userName.values("test name")
            .age.values(43)
            .eqTable();
    }

    @Test
    public void test_batchSave_ErrorPk() {
        ATM.dataMap.student.cleanTable();
        want.exception(() ->
            dao.save(Arrays.asList(
                new StudentEntity().setUserName("test name1").setAge(43),
                new StudentEntity().setUserName("test name2").setAge(43).setId(5L)
            )), FluentMybatisException.class, IllegalStateException.class
        ).contains("The instance primary keys in the list either have values or have no values");
    }

    @Test
    public void test_batchSave_WithPk() throws Exception {
        ATM.dataMap.student.cleanTable();
        dao.save(Arrays.asList(new StudentEntity().setId(4L).setUserName("test name1").setAge(43),
            new StudentEntity().setId(5L).setUserName("test name2").setAge(43)
        ));
        ATM.dataMap.student.table(2)
            .id.values(4, 5)
            .userName.values("test name1", "test name2")
            .age.values(43)
            .eqTable();
    }

    @Test
    public void test_batchSave_NoPk() throws Exception {
        ATM.dataMap.student.cleanTable();
        List<StudentEntity> list = list(
            new StudentEntity().setUserName("test name1").setAge(43),
            new StudentEntity().setUserName("test name2").setAge(43)
        );
        dao.save(list);
        want.number(list.get(0).getId()).notNull();
        want.number(list.get(1).getId()).notNull();
        ATM.dataMap.student.table(2)
            .id.values(list.get(0).getId(), list.get(1).getId())
            .userName.values("test name1", "test name2")
            .age.values(43)
            .eqTable();
    }
}
