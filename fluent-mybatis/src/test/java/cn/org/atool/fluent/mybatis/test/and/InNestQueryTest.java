package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.AddressQuery;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * InNestQueryTest
 *
 * @author darui.wu
 * @create 2020/6/19 10:54 下午
 */
public class InNestQueryTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    void test_and_in_nested() {
        UserQuery query = new UserQuery()
            .selectId()
            .and.id.in(q -> q.selectId().and.id.eq(3L));
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE id IN (SELECT id FROM t_user WHERE id = ?)");
    }

    @Test
    void test_and_in_nested2() {
        UserQuery query = new UserQuery()
            .selectId()
            .and.addressId.in(AddressQuery.class, q -> q.selectId().and.id.in(1L, 2L));
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE address_id IN (SELECT id FROM address WHERE id IN (?, ?))");
    }
}