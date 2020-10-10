package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.UserExtDao;
import cn.org.atool.fluent.mybatis.generate.DM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * @author darui.wu
 * @create 2019/10/29 9:36 下午
 */
public class DeleteByEntityIdsTest extends BaseTest {
    @Autowired
    private UserExtDao dao;

    @Test
    public void test_deleteByEntityIds() throws Exception {
        db.table(t_user).clean().insert(DM.user.initTable(10));
        dao.deleteByEntityIds(Arrays.asList(new UserEntity().setId(1L), new UserEntity().setId(5L)));
        db.sqlList().wantFirstSql().eq("DELETE FROM t_user WHERE id IN (?, ?)");
        db.table(t_user).count().isEqualTo(8);
    }
}
