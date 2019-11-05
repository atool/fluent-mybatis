package cn.org.atool.mybatis.fluent.test.and;

import cn.org.atool.mybatis.fluent.demo.mapper.UserMapper;
import cn.org.atool.mybatis.fluent.demo.query.UserEntityQuery;
import cn.org.atool.mybatis.fluent.test.BaseTest;
import org.junit.Test;
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
        db.sqlList().wantFirstSql()
                .where().eq("user_name LIKE ? ORDER BY id ASC , address_id ASC , user_name DESC , id+0 ASC");
    }
}
