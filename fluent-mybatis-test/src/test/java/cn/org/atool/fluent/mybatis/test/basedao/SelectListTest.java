package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.UserExtDao;
import cn.org.atool.fluent.mybatis.generate.DM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
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
    private UserExtDao dao;

    @Test
    public void test_selectList() throws Exception {
        db.table(t_user).clean().insert(DM.user.initTable(10)
            .userName.values(DataGenerator.increase("username_%d")));

        List<UserEntity> users = dao.selectList(3L, 6L, 7L);
        want.list(users).eqDataMap(DM.user.entity(3)
            .id.values(3, 6, 7)
            .userName.values("username_3", "username_6", "username_7")
        );
    }
}
