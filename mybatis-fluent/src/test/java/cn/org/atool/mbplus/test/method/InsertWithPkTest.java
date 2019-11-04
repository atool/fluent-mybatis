package cn.org.atool.mbplus.test.method;

import cn.org.atool.mbplus.demo.dm.table.UserTableMap;
import cn.org.atool.mbplus.demo.entity.UserEntity;
import cn.org.atool.mbplus.demo.mapper.UserMapper;
import cn.org.atool.mbplus.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InsertWithPkTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void testInsertWithPk() {
        db.table(t_user).clean();
        UserEntity user = new UserEntity()
                .setId(34L)
                .setUserName("user name")
                .setAge(25);
        mapper.insertWithPk(user);
        db.table(t_user).query().eqDataMap(new UserTableMap(1)
                .id.values(34L)
                .user_name.values("user name")
                .age.values(25));
    }
}
