package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.notgen.UserExtDao;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Arrays;

public class DeleteByIdTest extends BaseTest {
    @Autowired
    private UserExtDao dao;

    @Test
    public void test_deleteById() throws Exception {
        db.table(t_user).clean().insert(TM.user.createWithInit(10));
        dao.deleteById(4L);
        db.sqlList().wantFirstSql().eq("DELETE FROM t_user WHERE id=?");
        db.table(t_user).count().eq(9L);
    }

    @Test
    public void test_deleteByIds() throws Exception {
        db.table(t_user).clean().insert(TM.user.createWithInit(10));
        dao.deleteByIds(Arrays.asList(4L, 6L, 9L));
        db.sqlList().wantFirstSql().eq("DELETE FROM t_user WHERE id IN ( ? , ? , ? )", StringMode.SameAsSpace);
        db.table(t_user).count().eq(7L);
    }
}