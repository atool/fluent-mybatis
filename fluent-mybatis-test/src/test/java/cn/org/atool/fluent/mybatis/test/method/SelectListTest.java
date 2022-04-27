package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.tools.datagen.DataGenerator;

import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:34 下午
 */
public class SelectListTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_selectList() throws Exception {
        ATM.dataMap.student.initTable(10)
            .userName.values(DataGenerator.increase("username_%d"))
            .env.values("test_env")
            .cleanAndInsert();

        List<StudentEntity> users = dao.selectList(3L, 6L, 7L);
        want.list(users).eqDataMap(ATM.dataMap.student.entity(3)
            .id.values(3, 6, 7)
            .userName.values("username_3", "username_6", "username_7")
        );
    }

    @Test
    public void test_QueryDirectly() throws Exception {
        ATM.dataMap.student.initTable(10)
            .userName.values(DataGenerator.increase("username_%d"))
            .env.values("test_env")
            .cleanAndInsert();

        List<StudentEntity> users = StudentQuery.emptyQuery()
            .where.id().in(new long[]{3L, 6L, 7L}).end()
            .of(mapper).listEntity();

        want.list(users).eqDataMap(ATM.dataMap.student.entity(3)
            .id.values(3, 6, 7)
            .userName.values("username_3", "username_6", "username_7")
        );
    }

    @Test
    public void test_QueryDirectly2() throws Exception {
        ATM.dataMap.student.initTable(10)
            .userName.values(DataGenerator.increase("username_%d"))
            .env.values("test_env")
            .cleanAndInsert();

        List<StudentEntity> users = StudentQuery.emptyQuery()
            .where.id().in(new long[]{3L, 6L, 7L}).end()
            .to().listEntity();

        want.list(users).eqDataMap(ATM.dataMap.student.entity(3)
            .id.values(3, 6, 7)
            .userName.values("username_3", "username_6", "username_7")
        );
    }

    @Test
    public void test_UpdateDirectly() throws Exception {
        ATM.dataMap.student.initTable(3)
            .id.values(1, 2, 3)
            .userName.values("user_1", "user_2", "user_3")
            .env.values("test_env")
            .cleanAndInsert();

        StudentUpdate.emptyUpdater()
            .set.userName().is("test").end()
            .where.id().eq(2L)
            .and.env().eq("test_env").end()
            .of(mapper).updateBy();

        ATM.dataMap.student.table(3)
            .id.values(1, 2, 3)
            .userName.values("user_1", "test", "user_3")
            .env.values("test_env")
            .eqTable();
    }

    @Test
    public void test_UpdateDirectly2() throws Exception {
        ATM.dataMap.student.initTable(3)
            .id.values(1, 2, 3)
            .userName.values("user_1", "user_2", "user_3")
            .env.values("test_env")
            .cleanAndInsert();

        StudentUpdate.emptyUpdater()
            .set.userName().is("test").end()
            .where.id().eq(2L)
            .and.env().eq("test_env").end()
            .to().updateBy();

        ATM.dataMap.student.table(3)
            .id.values(1, 2, 3)
            .userName.values("user_1", "test", "user_3")
            .env.values("test_env")
            .eqTable();
    }
}
