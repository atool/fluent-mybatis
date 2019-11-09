package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.demo.generate.ITable;
import cn.org.atool.fluent.mybatis.demo.generate.datamap.table.UserTableMap;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserEntityUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class UpdateByQueryTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void testUpdate() {
        db.table(ITable.t_user).clean().insert(UserTableMap.createWithInit(2)
                .id.values(23L, 24L)
                .user_name.values("user1", "user2")
        );
        UserEntityUpdate update = new UserEntityUpdate()
                .set.userName.is("user name2")
                .and.id.eq(24L);
        mapper.updateBy(update);
        db.sqlList().wantFirstSql()
                .eq("UPDATE t_user SET gmt_modified=now(), user_name=? WHERE (id = ?)", StringMode.SameAsSpace);
        db.table(ITable.t_user).query().eqDataMap(UserTableMap.create(2)
                .id.values(23L, 24L)
                .user_name.values("user1", "user name2")
        );
    }
}