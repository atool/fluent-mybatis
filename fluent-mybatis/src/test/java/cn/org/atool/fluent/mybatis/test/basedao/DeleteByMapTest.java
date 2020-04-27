package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.notgen.UserExtDao;
import cn.org.atool.fluent.mybatis.demo.generate.datamap.table.UserTableMap;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.UserMP;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.HashMap;

/**
 * @author darui.wu
 * @create 2019/10/29 9:36 下午
 */
public class DeleteByMapTest extends BaseTest {
    @Autowired
    private UserExtDao dao;

    @Test
    public void test_deleteByMap() throws Exception {
        db.table(t_user).clean().insert(TM.user.createWithInit(10)
                .user_name.values("test1", "test12", "test3", "test12", "tess2")
        );
        dao.deleteByMap(new HashMap<String, Object>() {
            {
                this.put(UserMP.Column.user_name, "test12");
            }
        });
        db.sqlList().wantFirstSql().eq("DELETE FROM t_user WHERE (user_name = ?)", StringMode.SameAsSpace);
        db.table(t_user).count().eq(8);
    }
}
