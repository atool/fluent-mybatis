package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.UserExtDao;
import cn.org.atool.fluent.mybatis.generate.datamap.TM;
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
    private UserExtDao dao;

    @Test
    public void test_selectFields() throws Exception {
        db.table(t_user).clean()
                .insert(TM.user.createWithInit(10)
                        .user_name.values(DataGenerator.increase("username_%d"))
                );

        List<String> names = dao.selectFields(3L, 5L, 8L);
        want.list(names).eqReflect(new String[]{"username_3", "username_5", "username_8"});
    }
}