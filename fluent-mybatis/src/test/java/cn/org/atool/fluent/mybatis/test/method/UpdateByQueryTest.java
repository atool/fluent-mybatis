package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.demo.generate.ITable;
import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class UpdateByQueryTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void testUpdate() {
        db.table(ITable.t_user).clean().insert(TM.user.createWithInit(2)
            .id.values(23L, 24L)
            .user_name.values("user1", "user2")
        );
        UserUpdate update = new UserUpdate()
            .update.userName().is("user name2").end()
            .where.id().eq(24L)
            .apply("1=1").end();
        mapper.updateBy(update);
        db.sqlList().wantFirstSql()
            .eq("UPDATE t_user SET gmt_modified = now(), user_name = ? WHERE id = ? AND 1=1", StringMode.SameAsSpace);
        db.table(ITable.t_user).query().eqDataMap(TM.user.create(2)
            .id.values(23L, 24L)
            .user_name.values("user1", "user name2")
        );
    }

    @Test
    public void testUpdate_apply() {
        db.table(ITable.t_user).clean().insert(TM.user.createWithInit(2)
            .id.values(23L, 24L)
            .user_name.values("user1", "user2")
        );
        UserUpdate update = new UserUpdate()
            .set.userName().is("user name2").end()
            .where.id().eq(24L)
            .and.apply("user_name='user2'")
            .or.apply("user_name=?", "xxx").end();
        mapper.updateBy(update);
        db.sqlList().wantFirstSql()
            .eq("UPDATE t_user SET gmt_modified = now(), user_name = ? " +
                "WHERE id = ? " +
                "AND user_name='user2' " +
                "OR user_name=?", StringMode.SameAsSpace);
        db.table(ITable.t_user).query().eqDataMap(TM.user.create(2)
            .id.values(23L, 24L)
            .user_name.values("user1", "user name2")
        );
    }
}