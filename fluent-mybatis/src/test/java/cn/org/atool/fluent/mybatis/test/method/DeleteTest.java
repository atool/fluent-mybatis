package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.demo.generate.ITable;
import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class DeleteTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void testDeleteById() {
        db.table(ITable.t_user).clean().insert(TM.user.createWithInit(2)
            .id.values(23L, 24L)
            .user_name.values("user1", "user2")
        );
        UserQuery update = new UserQuery()
            .and.id.eq(24L);
        mapper.delete(update);
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM t_user WHERE id = ?", StringMode.SameAsSpace);
        db.table(ITable.t_user).query().eqDataMap(TM.user.create(1)
            .id.values(23L)
            .user_name.values("user1")
        );
    }

    @Test
    public void testDelete_apply() {
        db.table(ITable.t_user).clean().insert(TM.user.createWithInit(2)
            .id.values(23L, 24L)
            .user_name.values("user1", "user2")
        );
        UserQuery update = new UserQuery()
            .and("user_name=?", "user2");
        mapper.delete(update);
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM t_user WHERE user_name=?", StringMode.SameAsSpace);
        db.table(ITable.t_user).query().eqDataMap(TM.user.create(1)
            .id.values(23L)
            .user_name.values("user1")
        );
    }
}