package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.UserExtDao;
import cn.org.atool.fluent.mybatis.generate.DM;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class DeleteByQueryTest extends BaseTest {
    @Autowired
    private UserExtDao dao;

    @Test
    public void test_deleteByQuery() throws Exception {
        db.table(t_user).clean().insert(DM.user.initTable(10)
                .userName.values(DataGenerator.increase("username_%d")));
        dao.deleteByQuery("username_4", "username_5", "username_7");
        db.table(t_user).count().eq(7);
        db.sqlList().wantFirstSql()
                .eq("DELETE FROM t_user WHERE user_name IN (?, ?, ?)", StringMode.SameAsSpace);
    }
}
