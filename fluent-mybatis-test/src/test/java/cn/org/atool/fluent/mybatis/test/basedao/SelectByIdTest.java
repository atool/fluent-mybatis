package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.UserExtDao;
import cn.org.atool.fluent.mybatis.generate.DM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectByIdTest extends BaseTest {
    @Autowired
    private UserExtDao dao;

    @Test
    public void test_selectById() throws Exception {
        db.table(t_user).clean().insert(DM.user.initTable(3)
            .userName.values(DataGenerator.increase("username_%d")));
        UserEntity user = dao.selectById(3L);
        db.sqlList().wantFirstSql()
            .where().eq("id = ?");
        want.object(user)
            .eqMap(DM.user.entity()
                .userName.values("username_3")
            );
    }

    @Test
    public void test_selectByIds() throws Exception {
        db.table(t_user).clean().insert(DM.user.initTable(10)
            .userName.values(DataGenerator.increase("username_%d")));
        List<UserEntity> users = dao.selectByIds(Arrays.asList(3L, 5L));
        db.sqlList().wantFirstSql()
            .where().eq("id IN (?, ?)");
        want.object(users).eqDataMap(DM.user.entity(2)
            .userName.values("username_3", "username_5")
        );
    }
}
