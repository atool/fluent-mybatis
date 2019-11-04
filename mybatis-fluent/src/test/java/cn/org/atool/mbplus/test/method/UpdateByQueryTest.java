package cn.org.atool.mbplus.test.method;

import cn.org.atool.mbplus.demo.dm.table.UserTableMap;
import cn.org.atool.mbplus.demo.mapper.UserMapper;
import cn.org.atool.mbplus.demo.query.UserEntityUpdate;
import cn.org.atool.mbplus.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateByQueryTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void testUpdate() {
        db.table(t_user).clean().insert(new UserTableMap(2)
                .id.values(23L, 24L)
                .user_name.values("user1", "user2")
        );
        UserEntityUpdate update = new UserEntityUpdate()
                .set.userName.is("user name2")
                .and.id.eq(24L);
        mapper.updateBy(update);
        db.sqlList().wantFirstSql().eq("UPDATE t_user SET user_name=?, gmt_modified=now() WHERE id = ?");
        db.table(t_user).query().eqDataMap(new UserTableMap(2)
                .id.values(23L, 24L)
                .user_name.values("user1", "user name2")
        );
    }
}