package cn.org.atool.mybatis.fluent.test.basedao;

import cn.org.atool.mybatis.fluent.demo.dao.intf.UserDao;
import cn.org.atool.mybatis.fluent.demo.dm.table.UserTableMap;
import cn.org.atool.mybatis.fluent.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class DeleteByIdTest extends BaseTest {
    @Autowired
    private UserDao dao;

    @Test
    public void test_deleteById() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(10).init());
        dao.deleteById(4L);
        db.sqlList().wantFirstSql().eq("DELETE FROM t_user WHERE id=?");
        db.table(t_user).count().eq(9L);
    }

    @Test
    public void test_deleteByIds() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(10).init());
        dao.deleteByIds(Arrays.asList(4L, 6L, 9L));
        db.sqlList().wantFirstSql().eq("DELETE FROM t_user WHERE id IN ( ? , ? , ? )");
        db.table(t_user).count().eq(7L);
    }
}