package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generate.datamap.ITable;
import cn.org.atool.fluent.mybatis.generate.datamap.TM;
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
        db.table(ITable.t_user).clean().insert(TM.user.createWithInit(2)
                .id.values(23L, 24L)
                .user_name.values("user1", "user2")
        );

        UserEntity update = new UserEntity()
                .setAge(45)
                .setUserName("test name")
                .setIsDeleted(true)
                .setId(24L);

        mapper.updateById(update);
        db.sqlList().wantFirstSql()
                .eq("UPDATE t_user SET age=?, gmt_modified=now(), is_deleted=?, user_name=? WHERE id=?", StringMode.SameAsSpace);

        db.table(ITable.t_user).query().eqDataMap(TM.user.create(2)
                .id.values(23L, 24L)
                .user_name.values("user1", "test name")
                .age.values((Object) null, 45)
        );
    }

    @Test
    public void testUpdate_gmtCreate() {
        db.table(ITable.t_user).clean().insert(TM.user.createWithInit(2)
            .id.values(23L, 24L)
            .user_name.values("user1", "user2")
        );

        UserEntity update = new UserEntity()
            .setAge(45)
            .setUserName("test name")
            .setIsDeleted(true)
            .setId(24L)
            .setGmtCreated(new Date());

        mapper.updateById(update);
        db.sqlList().wantFirstSql()
            .eq("UPDATE t_user SET age=?, gmt_created=?, gmt_modified=now(), is_deleted=?, user_name=? WHERE id=?", StringMode.SameAsSpace);

        db.table(ITable.t_user).query().eqDataMap(TM.user.create(2)
            .id.values(23L, 24L)
            .user_name.values("user1", "test name")
            .age.values((Object) null, 45)
        );
    }

    @Test
    public void testUpdate2() {
        db.table(ITable.t_user).clean().insert(TM.user.createWithInit(2)
                .id.values(23L, 24L)
                .user_name.values("user1", "user2")
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
                .eq("UPDATE t_user SET age=?, gmt_created=?, gmt_modified=now(), is_deleted=?, user_name=? WHERE id=?", StringMode.SameAsSpace);
        db.table(ITable.t_user).query().eqDataMap(TM.user.create(2)
                .id.values(23L, 24L)
                .user_name.values("user1", "test name")
                .age.values((Object) null, 45)
        );
    }
}