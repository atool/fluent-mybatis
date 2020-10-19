package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class DeleteTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void testDeleteById() {
        db.table(ATM.Table.user).clean().insert(ATM.DataMap.user.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
        );
        UserQuery update = new UserQuery()
            .where.id().eq(24L).end();
        mapper.delete(update);
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM t_user WHERE id = ?", StringMode.SameAsSpace);
        db.table(ATM.Table.user).query().eqDataMap(ATM.DataMap.user.table(1)
            .id.values(23L)
            .userName.values("user1")
        );
    }

    @Test
    public void testDelete_apply() {
        db.table(ATM.Table.user).clean().insert(ATM.DataMap.user.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
        );
        UserQuery update = new UserQuery()
            .where.apply("user_name=?", "user2").end();
        mapper.delete(update);
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM t_user WHERE user_name=?", StringMode.SameAsSpace);
        db.table(ATM.Table.user).query().eqDataMap(ATM.DataMap.user.table(1)
            .id.values(23L)
            .userName.values("user1")
        );
    }
}
