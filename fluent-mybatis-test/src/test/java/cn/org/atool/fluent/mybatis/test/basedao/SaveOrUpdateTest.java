package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.customize.UserExtDao;
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
        db.table(t_user).clean().insert(TM.user.createWithInit(3));
        dao.saveOrUpdate(new UserEntity().setId(3L).setUserName("test_111").setAge(30));
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE id = ?", StringMode.SameAsSpace);
        db.sqlList().wantSql(1).eq("UPDATE t_user SET age=?, gmt_modified=now(), user_name=? WHERE id=?");
        db.table(t_user).queryWhere("id=3")
                .eqDataMap(TM.user.create(1)
                        .user_name.values("test_111")
                        .age.values(30)
                );
    }

    @Test
    public void test_saveOrUpdate_2() throws Exception {
        db.table(t_user).clean().insert(TM.user.createWithInit(3));
        dao.saveOrUpdate(new UserEntity().setId(4L).setUserName("test_111").setAge(30));
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE id = ?", StringMode.SameAsSpace);
        db.sqlList().wantSql(1).contains("INSERT INTO t_user");
        db.table(t_user).count().eq(4);
        db.table(t_user).queryWhere("id=4")
                .eqDataMap(TM.user.create(1)
                        .user_name.values("test_111")
                        .age.values(30)
                );
    }
}