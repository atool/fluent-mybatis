package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.UserExtDao;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.generate.helper.UserMapping;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectByMapTest extends BaseTest {
    @Autowired
    private UserExtDao dao;

    @Test
    public void test_selectByMap() throws Exception {
        db.table(ATM.Table.user).clean()
            .insert(ATM.DataMap.user.initTable(10)
                .userName.values(DataGenerator.increase("username_%d"))
            );

        List<UserEntity> users = dao.selectByMap(new HashMap<String, Object>() {
            {
                this.put(UserMapping.userName.column, "username_4");
            }
        });
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_user WHERE user_name = ?");
        want.list(users).eqDataMap(ATM.DataMap.user.entity(1)
            .userName.values("username_4"));
    }
}
