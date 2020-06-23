package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.AddressMapper;
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
public class NotInNestQueryTest extends BaseTest {
    @Autowired
    private AddressMapper mapper;

    @Test
    void test_and_not_in_nested() {
        AddressQuery query = new AddressQuery()
            .selectId()
            .where.id().notIn(q -> q.selectId().where.id().eq(3L).end())
            .end();
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM address WHERE id NOT IN (SELECT id FROM address WHERE id = ?)");
    }

    @Test
    void test_and_in_nested2() {
        AddressQuery query = new AddressQuery()
            .selectId()
            .where
            .id().notIn(UserQuery.class, q -> q
                .select("address_id")
                .where
                .age().eq(24)
                .end())
            .end();
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM address WHERE id NOT IN (SELECT address_id FROM t_user WHERE age = ?)");
    }
}