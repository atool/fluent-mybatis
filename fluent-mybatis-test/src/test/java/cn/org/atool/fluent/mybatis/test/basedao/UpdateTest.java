package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.customize.UserExtDao;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO:类说明
 *
 * @author darui.wu
 * @create 2019/10/31 11:16 上午
 */
public class UpdateTest extends BaseTest {
    @Autowired
    private UserExtDao dao;

    @Test
    public void test_update() throws Exception {
        db.table(t_user).clean().insert(TM.user.createWithInit(5)
                .user_name.values(DataGenerator.increase("username_%d")));
        dao.updateUserNameById("new_user_name", 4L);
        db.sqlList().wantFirstSql().eq("UPDATE t_user SET gmt_modified = now(), user_name = ? WHERE id = ?");
        db.table(t_user).queryWhere("id=4")
                .eqDataMap(TM.user.create(1)
                        .user_name.values("new_user_name")
                );
    }
}