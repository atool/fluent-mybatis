package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserEntityQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void order() {
        UserEntityQuery query = new UserEntityQuery()
                .and.userName.like("user")
                .orderBy.id.asc().addressId.asc().userName.desc().endOrder()
                .orderByAsc("id+0");
        mapper.selectList(query);
        db.sqlList().wantFirstSql().where().eq("(user_name LIKE ?)");
        db.sqlList().wantFirstSql().end("ORDER BY id ASC , address_id ASC , user_name DESC , id+0 ASC");
    }
}
