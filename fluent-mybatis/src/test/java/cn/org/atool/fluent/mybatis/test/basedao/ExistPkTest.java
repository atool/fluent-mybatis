package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.demo.notgen.UserExtDao;
import cn.org.atool.fluent.mybatis.demo.datamap.table.UserTableMap;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

/**
 * @author darui.wu
 * @create 2019/10/29 5:38 下午
 */
public class ExistPkTest extends BaseTest {
    @Autowired
    private UserExtDao dao;

    @Test
    public void test_exist() {
        db.table(t_user).clean().insert(UserTableMap.create(2)
                .id.values(1, 3)
        );
        boolean existed = dao.existPk(1);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE (id = ?)", StringMode.SameAsSpace);
        want.bool(existed).is(true);

        existed = dao.existPk(2);
        want.bool(existed).is(false);
    }
}
