package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * AndNestedTest
 *
 * @author darui.wu
 * @create 2020/6/19 12:27 上午
 */
public class AndNestedTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    void test_and_nested() {
        UserQuery query = new UserQuery()
            .where
            .id().in(q -> q.selectId().where.id().eq(3L).end())
            .and(q -> q
                .where
                .age().eq(24)
                .id().eq(3L)
                .end()
            )
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT( * ) FROM t_user WHERE id IN (SELECT id FROM t_user WHERE id = ?) AND ( age = ? AND id = ? )");

    }

    @Test
    void test_or_nested() {
        UserQuery query = new UserQuery()
            .where
            .id().in(q -> q.selectId().where.id().eq(3L).end())
            .or(q -> q.where
                .or(q2 -> q2.where
                    .age().eq(24)
                    .or()
                    .id().eq(3L)
                    .id().eq(3)
                    .end()
                )
                .and(q1 -> q1.where.id().eq(2L).end()
                ).end()).end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT( * ) FROM t_user " +
                "WHERE id IN (SELECT id FROM t_user WHERE id = ?) " +
                "OR ( ( age = ? OR id = ? AND id = ? ) " +
                "AND ( id = ? ) )");
    }
}