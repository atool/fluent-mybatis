package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * InsertSelectedTest
 *
 * @author darui.wu
 * @create 2020/5/13 1:19 下午
 */
public class InsertSelectedTest extends BaseTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        db.table(t_user).clean();
        UserEntity user = new UserEntity()
            .setAge(23)
            .setUserName("tom mike");
        userMapper.insert(user);
        db.table(t_user).query().eqDataMap(TM.user.create(1)
            .age.values(23)
            .user_name.values("tom mike")
        );
        db.sqlList().wantFirstSql().eq("INSERT INTO t_user ( age, gmt_created, gmt_modified, is_deleted, user_name ) VALUES ( ?, now(), now(), 0, ? )");
        want.number(user.getId()).isGt(0L);
    }

    @Test
    void testInsert_withId() {
        db.table(t_user).clean();
        UserEntity user = new UserEntity()
            .setUserName("tom mike")
            .setId(100L)
            .setAddressId(200L);
        userMapper.insert(user);
        db.table(t_user).query().eqDataMap(TM.user.create(1)
            .id.values(100)
            .user_name.values("tom mike")
            .address_id.values(200)
        );
        db.sqlList().wantFirstSql().eq("INSERT INTO t_user ( id, address_id, gmt_created, gmt_modified, is_deleted, user_name ) VALUES ( ?, ?, now(), now(), 0, ? )");
        want.number(user.getId()).eq(100L);
    }
}