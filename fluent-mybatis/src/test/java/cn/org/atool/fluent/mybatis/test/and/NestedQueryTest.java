package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.AddressQuery;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * NestedQueryTest
 *
 * @author darui.wu
 * @create 2020/6/19 8:28 下午
 */
public class NestedQueryTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    void test_or_nested() {
        UserQuery query = new UserQuery()
            .selectId()
            .and.exists(AddressQuery.class, q -> q
                .and.address.like("u")
                .and.id.apply("=t_user.address_id")
            );
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE EXISTS (SELECT * FROM address WHERE address LIKE ? AND id =t_user.address_id)");

    }

    @Test
    void test_exist() {
        UserQuery query = new UserQuery()
            .selectId()
            .and.exists(q -> q.selectId()
                .and.id.eq(34L)
            );
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE EXISTS (SELECT id FROM t_user WHERE id = ?)");

    }

    @Test
    void test_not_exist() {
        UserQuery query = new UserQuery()
            .selectId()
            .and.notExists(q -> q.selectId()
                .and.id.eq(34L)
            );
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE NOT EXISTS (SELECT id FROM t_user WHERE id = ?)");

    }
}