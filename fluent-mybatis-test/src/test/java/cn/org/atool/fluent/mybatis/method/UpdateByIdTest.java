package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Date;

/**
 * @author darui.wu
 * @create 2020/1/2 4:37 下午
 */
public class UpdateByIdTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void testUpdate() {
        db.table(ATM.Table.user).clean().insert(ATM.DataMap.user.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
        );

        UserEntity update = new UserEntity()
            .setAge(45)
            .setUserName("test name")
            .setIsDeleted(true)
            .setId(24L);

        mapper.updateById(update);
        db.sqlList().wantFirstSql()
            .eq("UPDATE t_user SET gmt_modified = now(), is_deleted = ?, age = ?, user_name = ? WHERE id = ?", StringMode.SameAsSpace);

        db.table(ATM.Table.user).query().eqDataMap(ATM.DataMap.user.table(2)
            .id.values(23L, 24L)
            .userName.values("user1", "test name")
            .age.values((Object) null, 45)
        );
    }

    @Test
    public void testUpdate_gmtCreate() {
        db.table(ATM.Table.user).clean().insert(ATM.DataMap.user.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
        );

        UserEntity update = new UserEntity()
            .setAge(45)
            .setUserName("test name")
            .setIsDeleted(true)
            .setId(24L)
            .setGmtCreated(new Date());

        mapper.updateById(update);
        db.sqlList().wantFirstSql()
            .eq("UPDATE t_user SET gmt_created = ?, gmt_modified = now(), is_deleted = ?, age = ?, user_name = ? WHERE id = ?", StringMode.SameAsSpace);

        db.table(ATM.Table.user).query().eqDataMap(ATM.DataMap.user.table(2)
            .id.values(23L, 24L)
            .userName.values("user1", "test name")
            .age.values((Object) null, 45)
        );
    }

    @Test
    public void testUpdate2() {
        db.table(ATM.Table.user).clean().insert(ATM.DataMap.user.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
        );

        UserEntity update = new UserEntity()
            .setAge(45)
            .setUserName("test name")
            .setIsDeleted(true)
            .setId(24L)
            .setGmtModified(new Date())
            .setGmtCreated(new Date());

        mapper.updateById(update);
        db.sqlList().wantFirstSql()
            .eq("UPDATE t_user SET gmt_created = ?, gmt_modified = ?, is_deleted = ?, age = ?, user_name = ? WHERE id = ?", StringMode.SameAsSpace);
        db.table(ATM.Table.user).query().eqDataMap(ATM.DataMap.user.table(2)
            .id.values(23L, 24L)
            .userName.values("user1", "test name")
            .age.values((Object) null, 45)
        );
    }
}
