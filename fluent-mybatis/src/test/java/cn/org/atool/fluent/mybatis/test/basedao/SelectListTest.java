package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.EM;
import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.notgen.UserExtDao;
import cn.org.atool.fluent.mybatis.demo.generate.datamap.entity.UserEntityMap;
import cn.org.atool.fluent.mybatis.demo.generate.datamap.table.UserTableMap;
import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
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
        db.table(t_user).clean().insert(TM.user.createWithInit(10)
                .user_name.values(DataGenerator.increase("username_%d")));

        List<UserEntity> users = dao.selectList(3L, 6L, 7L);
        want.list(users).eqDataMap(EM.user.create(3)
                .id.values(3, 6, 7)
                .userName.values("username_3", "username_6", "username_7")
        );
    }
}