package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.datamap.ITable;
import cn.org.atool.fluent.mybatis.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InsertWithPkTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void testInsertWithPk() {
        db.table(ITable.t_user).clean();
        UserEntity user = new UserEntity()
                .setId(34L)
                .setUserName("user name")
                .setAge(25);
        mapper.insert(user);
        db.table(ITable.t_user)
                .query()
                .eqDataMap(TM.user.create(1)
                        .id.values(34L)
                        .user_name.values("user name")
                        .age.values(25)
                );
    }
}