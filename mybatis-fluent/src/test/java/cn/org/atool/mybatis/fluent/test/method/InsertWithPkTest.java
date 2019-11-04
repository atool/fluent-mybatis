package cn.org.atool.mybatis.fluent.test.method;

import cn.org.atool.mybatis.fluent.demo.dm.table.UserTableMap;
import cn.org.atool.mybatis.fluent.demo.entity.UserEntity;
import cn.org.atool.mybatis.fluent.demo.mapper.UserMapper;
import cn.org.atool.mybatis.fluent.test.BaseTest;
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
