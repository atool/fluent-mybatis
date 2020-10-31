package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:34 下午
 */
public class SelectFieldsTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_selectFields() throws Exception {
        db.table(ATM.Table.student).clean()
                .insert(ATM.DataMap.student.initTable(10)
                        .userName.values(DataGenerator.increase("username_%d"))
                );

        List<String> names = dao.selectFields(3L, 5L, 8L);
        want.list(names).eqReflect(new String[]{"username_3", "username_5", "username_8"});
    }
}
