package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.helper.AddressMapping;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.AddressQuery;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static cn.org.atool.fluent.mybatis.demo.generate.helper.AddressMapping.userId;

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
            .where.exists(AddressQuery.class, q -> q
                .where.address().like("u")
                .and.id().apply("=t_user.address_id").end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE EXISTS (SELECT * FROM address WHERE address LIKE ? AND id =t_user.address_id)");

    }

    @Test
    void test_exist() {
        UserQuery query = new UserQuery()
            .selectId()
            .where.exists(q -> q.selectId()
                .where.id().eq(34L).end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE EXISTS (SELECT id FROM t_user WHERE id = ?)");
    }

    @DisplayName("嵌套查询：地址包含'杭州滨江'的所有用户列表")
    @Test
    void test_nested_query_address_like() {
        UserQuery query = new UserQuery()
            .where.id().in(AddressQuery.class, q -> q
                .select.apply(userId).end()
                .where.address().like("杭州滨江").end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id, address_id, age, gmt_created, gmt_modified, grade, is_deleted, user_name, version " +
                "FROM t_user " +
                "WHERE id IN (SELECT user_id FROM address WHERE address LIKE ?)");
    }
}