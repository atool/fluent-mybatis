package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.UserExtDao;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

/**
 * @author darui.wu
 * @create 2019/10/29 9:32 下午
 */
public class SaveOrUpdateTest extends BaseTest {
    @Autowired
    private UserExtDao dao;

    @Test
    public void test_saveOrUpdate() throws Exception {
        db.table(ATM.Table.user).clean().insert(ATM.DataMap.user.initTable(3));
        dao.saveOrUpdate(new UserEntity().setId(3L).setUserName("test_111").setAge(30));
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE id = ?", StringMode.SameAsSpace);
        db.sqlList().wantSql(1).eq("UPDATE t_user SET gmt_modified = now(), age = ?, user_name = ? WHERE id = ?");
        db.table(ATM.Table.user).queryWhere("id=3")
            .eqDataMap(ATM.DataMap.user.table(1)
                .userName.values("test_111")
                .age.values(30)
            );
    }

    @Test
    public void test_saveOrUpdate_2() throws Exception {
        db.table(ATM.Table.user).clean().insert(ATM.DataMap.user.initTable(3));
        dao.saveOrUpdate(new UserEntity().setId(4L).setUserName("test_111").setAge(30));
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE id = ?", StringMode.SameAsSpace);
        db.sqlList().wantSql(1).contains("INSERT INTO t_user");
        db.table(ATM.Table.user).count().eq(4);
        db.table(ATM.Table.user).queryWhere("id=4")
            .eqDataMap(ATM.DataMap.user.table(1)
                .userName.values("test_111")
                .age.values(30)
            );
    }
}
