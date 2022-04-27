package cn.org.atool.fluent.mybatis.test.where.nested;

import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

/**
 * NestedQueryTest
 *
 * @author darui.wu 2020/6/19 8:28 下午
 */
public class NestedQueryTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @DisplayName(".and(子查询) 中lambda表达式别名问题")
    @Test
    void test_I3YX65() {
        StudentQuery query = StudentQuery.emptyQuery("t1")
            .selectId()
            .where.id().in(new int[]{1, 3, 5})
            .and(q -> q.where.address().like("kk").or.age().ge(20).end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT t1.`id` " +
            "FROM fluent_mybatis.student t1 " +
            "WHERE t1.`id` IN (?, ?, ?) " +
            "AND (t1.`address` LIKE ? OR t1.`age` >= ?)", StringMode.SameAsSpace);
    }

    @Test
    void test_or_nested() {
        StudentQuery query = StudentQuery.emptyQuery()
            .selectId()
            .where.exists(HomeAddressQuery.emptyQuery()
                .where.address().like("u")
                .and.id().apply("=student.home_address_id").end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().containsInOrder(
            "SELECT `id` FROM fluent_mybatis.student WHERE EXISTS (SELECT `id`, `",
            "` FROM `home_address` WHERE `address` LIKE ? AND `id` =student.home_address_id)"
        );
    }

    @Test
    void test_or_nested_query() {
        StudentQuery query = StudentQuery.emptyQuery()
            .selectId()
            .where.exists(HomeAddressQuery.emptyQuery()
                .select.apply("1").end()
                .where.address().like("u")
                .and.id().apply("=student.home_address_id").end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` FROM fluent_mybatis.student " +
                "WHERE EXISTS (SELECT 1 " +
                "FROM `home_address` " +
                "WHERE `address` LIKE ? " +
                "AND `id` =student.home_address_id)");
    }

    @Test
    void test_exist() {
        StudentQuery query = StudentQuery.emptyQuery()
            .selectId()
            .where.exists(q -> q.selectId()
                .where.id().eq(34L).end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` FROM fluent_mybatis.student " +
                "WHERE EXISTS (SELECT `id` FROM fluent_mybatis.student WHERE `id` = ?)");
    }

    @DisplayName("嵌套查询：地址包含'杭州滨江'的所有用户列表")
    @Test
    void test_nested_query_address_like() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().in(HomeAddressQuery.emptyQuery()
                .select.studentId().end()
                .where.address().like("杭州滨江").end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .start("SELECT `id`, `address`,")
            .end(", `gmt_created`, `gmt_modified`, `is_deleted` FROM fluent_mybatis.student " +
                "WHERE `id` IN (SELECT `student_id` " +
                "   FROM `home_address` WHERE `address` LIKE ?)", StringMode.SameAsSpace);
    }
}
