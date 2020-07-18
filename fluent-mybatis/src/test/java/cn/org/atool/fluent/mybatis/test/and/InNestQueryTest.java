package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.helper.UserMapping;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.AddressQuery;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static cn.org.atool.fluent.mybatis.demo.generate.helper.UserMapping.id;

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
            .select.apply(id).sum.age().end()
            .where.id().in(q -> q.selectId()
                .where.id().eq(3L).end())
            .and.userName().like("user")
            .and.age().gt(23).end()
            .groupBy.id().end()
            .having.sum.age().gt(2)
            .and.sum.age().le(4).end();

        List list = mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id, SUM(age) FROM t_user " +
                "WHERE " +
                "id IN (SELECT id FROM t_user WHERE id = ?) " +
                "AND user_name LIKE ? " +
                "AND age > ? " +
                "GROUP BY id " +
                "HAVING SUM(age) > ? AND SUM(age) <= ?");
    }

    @Test
    void test_and_in_nested_1() {
        UserQuery query = new UserQuery()
            .where.id().in(q -> q.selectId()
                .where.id().eq(3L).end())
            .and.userName().like("user").end();

        List list = mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .end("WHERE id IN (SELECT id FROM t_user WHERE id = ?) AND user_name LIKE ?");
    }

    @Test
    void test_and_in_nested2() {
        UserQuery query = new UserQuery()
            .selectId()
            .where.addressId().in(AddressQuery.class, q -> q.selectId()
                .where.id().in(new int[]{1, 2}).end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE address_id IN (SELECT id FROM address WHERE id IN (?, ?))");
    }
}