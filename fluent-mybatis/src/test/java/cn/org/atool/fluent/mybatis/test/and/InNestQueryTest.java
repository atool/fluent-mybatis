package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.AddressQuery;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
            .select.id().as().age().sum().end()
            .where
            .id().in(q -> q
                .selectId()
                .where
                .id().eq(3L).end())
            .userName().like("user")
            .age().gt(23).end()
            .groupBy
            .id().end()
            .having
            .age().sum().gt(2).end();

        List list = mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id, SUM(age) FROM t_user " +
                "WHERE " +
                "id IN (SELECT id FROM t_user WHERE id = ?) " +
                "AND user_name LIKE ? " +
                "AND age > ? " +
                "GROUP BY id " +
                "HAVING SUM(age) > ?");
    }

    @Test
    void test_and_in_nested2() {
        UserQuery query = new UserQuery()
            .selectId()
            .where.addressId().in(AddressQuery.class, q -> q.selectId().where.id().in(new Integer[]{1, 2}).end())
            .end();
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE address_id IN (SELECT id FROM address WHERE id IN (?, ?))");
    }
}