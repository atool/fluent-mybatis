package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.demo.dao.intf.UserDao;
import cn.org.atool.fluent.mybatis.demo.dm.table.UserTableMap;
import cn.org.atool.fluent.mybatis.demo.entity.UserEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

/**
 * @author darui.wu
 * @create 2019/10/29 9:35 下午
 */
public class UpdateByEntityIdTest extends BaseTest {
    @Autowired
    private UserDao dao;

    @Test
    public void test_byEntityId() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(5).init());
        dao.updateById(new UserEntity().setId(2L).setUserName("test3").setAge(30));
        db.sqlList().wantFirstSql()
                .eq("UPDATE t_user SET gmt_modified=now(), user_name=?, age=? WHERE id=?", StringMode.SameAsSpace);
        db.table(t_user).queryWhere("id=2")
                .eqDataMap(new UserTableMap(1)
                        .user_name.values("test3")
                        .age.values(30)
                );
    }
}
