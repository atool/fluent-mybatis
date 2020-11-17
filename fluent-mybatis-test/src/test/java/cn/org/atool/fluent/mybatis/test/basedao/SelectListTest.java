package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:34 下午
 */
public class SelectListTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

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
}
