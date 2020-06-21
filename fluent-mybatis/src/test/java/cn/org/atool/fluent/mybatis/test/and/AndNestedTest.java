package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
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
            .and.id.in(q -> q.selectId().and.id.eq(3L))
            .and(q -> q
                .and.age.eq(24)
                .and.id.eq(3L)
            );
        mapper.selectCount(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT( * ) FROM t_user WHERE id IN (SELECT id FROM t_user WHERE id = ?) AND ( age = ? AND id = ? )");

    }

    @Test
    void test_or_nested() {
        UserQuery query = new UserQuery()
            .and.id.in(q -> q.selectId().and.id.eq(3L))
            .or(q -> q
                .and(q2 -> q2.or.age.eq(24)
                    .or.id.eq(3L))
                .and(q1 -> q1.and.id.eq(2L))
            );
        mapper.selectCount(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT( * ) FROM t_user WHERE id IN (SELECT id FROM t_user WHERE id = ?) OR ( ( age = ? OR id = ? ) AND ( id = ? ) )");

    }
}