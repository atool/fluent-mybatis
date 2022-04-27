package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.tools.datagen.DataGenerator;

import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:34 下午
 */
public class SelectObjsTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_selectObjs() throws Exception {
        ATM.dataMap.student.initTable(10)
            .userName.values(DataGenerator.increase("username_%d"))
            .env.values("test_env")
            .cleanAndInsert();

        List<String> names = dao.selectObjs(2L, 3L, 5L);
        want.list(names).eqReflect(new String[]{"username_2", "username_3", "username_5"});
    }

    @Test
    public void test_selectObjs_2() throws Exception {
        ATM.dataMap.student.initTable(1)
            .userName.values((Object) null)
            .env.values("test_env")
            .cleanAndInsert();

        List<String> names = dao.selectObjs(1L);
        want.list(names).eqReflect(new String[]{null});
    }

    @Test
    public void test_selectObjs2() throws Exception {
        ATM.dataMap.student.initTable(1)
            .userName.values((Object) null)
            .age.values((Object) null)
            .env.values("test_env")
            .cleanAndInsert();

        List<String> names = dao.selectObjs2(1L);
        want.list(names).eqReflect(new String[]{null});
    }
}
