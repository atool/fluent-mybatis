package cn.org.atool.fluent.mybatis.test.nested;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
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
    private StudentMapper mapper;

    @Test
    void test_and_nested() {
        StudentQuery query = new StudentQuery()
            .where.id().in(q -> q
                .selectId()
                .where.id().eq(3L).end())
            .and(q -> q
                .where.age().eq(24)
                .and.id().eq(3L).end()
            )
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) " +
                "FROM student " +
                "WHERE `id` IN (SELECT id FROM student WHERE `id` = ?) " +
                "AND ( `age` = ? AND `id` = ? )");
    }

    @Test
    void test_and_nested_query() {
        StudentQuery query = new StudentQuery()
            .where.id().in(new StudentQuery()
                .selectId()
                .where.id().eq(3L).end())
            .and(new StudentQuery()
                .where.age().eq(24)
                .and.id().eq(3L).end()
            )
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) " +
                "FROM student " +
                "WHERE `id` IN (SELECT id FROM student WHERE `id` = ?) " +
                "AND ( `age` = ? AND `id` = ? )");
    }

    @DisplayName("And嵌套查询为空的场景")
    @Test
    void test_and_nested_is_null() {
        StudentQuery query = new StudentQuery()
            .where.id().in(q -> q
                .selectId()
                .where.id().eq(3L).end())
            .and(q -> q
                .where.age().eq(24, If::everFalse)
                .or.id().eq(3L, If::everFalse).end()
            )
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM student " +
                "WHERE `id` IN (SELECT id FROM student WHERE `id` = ?)");
    }

    @DisplayName("Or嵌套查询为空的场景")
    @Test
    void test_or_nested_is_null() {
        StudentQuery query = new StudentQuery()
            .where
            .id().in(q -> q.selectId().where.id().eq(3L).end())
            .or(q -> q
                .where.age().eq(24, o -> false)
                .and.id().eq(3L, o -> false).end()
            )
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM student " +
                "WHERE `id` IN (SELECT id FROM student WHERE `id` = ?)");
    }

    @Test
    void test_or_nested() {
        StudentQuery query = new StudentQuery()
            .where.id().in(q -> q
                .selectId()
                .where.id().eq(3L).end())
            .or(q1 -> q1
                .where.and(q2 -> q2
                    .where.age().eq(24)
                    .or.id().eq(3L)
                    .and.id().eq(3).end())
                .and(q2 -> q2
                    .where.id().eq(2L)
                    .or.id().eq(4L).end())
                .end())
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM student " +
                "WHERE `id` IN (SELECT id FROM student WHERE `id` = ?) " +
                "OR ( ( `age` = ? OR `id` = ? AND `id` = ? ) AND ( `id` = ? OR `id` = ? ) )");
    }
}
