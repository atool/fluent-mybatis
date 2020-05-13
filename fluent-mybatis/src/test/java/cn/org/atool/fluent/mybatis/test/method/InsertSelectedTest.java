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
        userMapper.insertSelected(new UserEntity()
            .setAge(23)
            .setUserName("tom mike")
        );
        db.table(t_user).query().eqDataMap(TM.user.create(1)
            .age.values(23)
            .user_name.values("tom mike")
        );
        db.sqlList().wantFirstSql().eq("INSERT INTO t_user ( gmt_modified, is_deleted, gmt_created, user_name, age ) VALUES ( now(), 0, now(), ?, ? )");
    }

    @Test
    void testInsert_withId() {
        db.table(t_user).clean();
        userMapper.insertSelected(new UserEntity()
            .setUserName("tom mike")
            .setId(100L)
            .setAddressId(200L)
        );
        db.table(t_user).query().eqDataMap(TM.user.create(1)
            .id.values(100)
            .user_name.values("tom mike")
            .address_id.values(200)
        );
        db.sqlList().wantFirstSql().eq("INSERT INTO t_user ( id, gmt_modified, is_deleted, gmt_created, user_name, address_id ) VALUES ( ?, now(), 0, now(), ?, ? )");
    }
}