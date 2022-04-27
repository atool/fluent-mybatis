package cn.org.atool.fluent.mybatis.test.where.nested;

import cn.org.atool.fluent.mybatis.generator.shared2.mapper.HomeAddressMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
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
    private HomeAddressMapper mapper;

    @Test
    void test_and_not_in_nested() {
        HomeAddressQuery query = HomeAddressQuery.emptyQuery()
            .selectId()
            .where.id().notIn(q -> q.selectId()
                .where.id().eq(3L).end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` FROM `home_address` " +
                "WHERE `id` NOT IN (SELECT `id` FROM `home_address` WHERE `id` = ?)");
    }

    @Test
    void test_and_in_nested2() {
        HomeAddressQuery query = HomeAddressQuery.emptyQuery()
            .selectId()
            .where.id().notIn(StudentQuery.emptyQuery()
                .select.homeAddressId().end()
                .where.age().eq(24).end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` FROM `home_address` " +
                "WHERE `id` NOT IN (SELECT `home_address_id` FROM fluent_mybatis.student WHERE `age` = ?)");
    }
}
