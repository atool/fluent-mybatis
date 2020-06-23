package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.notgen.UserExtDao;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectOneTest extends BaseTest {
    @Autowired
    private UserExtDao dao;

    @Test
    public void test_selectOne() throws Exception {
        db.table(t_user).clean().insert(TM.user.createWithInit(10)
                .user_name.values(DataGenerator.increase("username_%d")));

        UserEntity user = dao.selectOne("username");
        want.object(user).notNull();
        db.sqlList().wantFirstSql().start("SELECT")
                .end("FROM t_user WHERE user_name LIKE ? LIMIT ?, ?", StringMode.SameAsSpace);
    }

    @Test
    public void test_selectOne2() throws Exception {
        db.table(t_user).clean().insert(TM.user.createWithInit(10)
                .user_name.values(DataGenerator.increase("username_%d")));

        String username = dao.selectOne(5);
        want.string(username).eq("username_5");
        db.sqlList().wantFirstSql().start("SELECT")
                .end("FROM t_user WHERE id = ? LIMIT ?, ?", StringMode.SameAsSpace);
    }
}