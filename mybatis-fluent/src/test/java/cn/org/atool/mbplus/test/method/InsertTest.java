package cn.org.atool.mbplus.test.method;

import cn.org.atool.mbplus.demo.dm.table.UserTableMap;
import cn.org.atool.mbplus.demo.entity.UserEntity;
import cn.org.atool.mbplus.demo.mapper.UserMapper;
import cn.org.atool.mbplus.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InsertTest extends BaseTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        db.table(t_user).clean();
        userMapper.insert(new UserEntity()
                .setAge(23)
                .setUserName("tom mike")
        );
        db.table(t_user).query().eqDataMap(UserTableMap.create(1)
                .age.values(23)
                .user_name.values("tom mike")
        );
    }
}
