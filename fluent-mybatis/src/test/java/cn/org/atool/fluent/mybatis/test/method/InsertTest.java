package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.demo.dm.table.UserTableMap;
import cn.org.atool.fluent.mybatis.demo.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
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
