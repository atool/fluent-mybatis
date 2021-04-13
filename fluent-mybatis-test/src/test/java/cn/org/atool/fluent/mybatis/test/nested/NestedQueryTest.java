package cn.org.atool.fluent.mybatis.test.nested;

import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static cn.org.atool.fluent.mybatis.generate.helper.HomeAddressMapping.studentId;

/**
 * NestedQueryTest
 *
 * @author darui.wu
 * @create 2020/6/19 8:28 下午
 */
public class NestedQueryTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void test_or_nested() {
        StudentQuery query = new StudentQuery()
            .selectId()
            .where.exists(HomeAddressQuery.class, q -> q
                .where.address().like("u")
                .and.id().apply("=student.home_address_id").end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM student WHERE EXISTS (SELECT * FROM home_address WHERE address LIKE ? AND id =student.home_address_id)");
    }

    @Test
    void test_or_nested_query() {
        StudentQuery query = new StudentQuery()
            .selectId()
            .where.exists(new HomeAddressQuery()
                .select.apply("1").end()
                .where.address().like("u")
                .and.id().apply("=student.home_address_id").end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM student WHERE EXISTS (SELECT 1 FROM home_address WHERE address LIKE ? AND id =student.home_address_id)");
    }

    @Test
    void test_exist() {
        StudentQuery query = new StudentQuery()
            .selectId()
            .where.exists(q -> q.selectId()
                .where.id().eq(34L).end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM student WHERE EXISTS (SELECT id FROM student WHERE id = ?)");
    }

    @DisplayName("嵌套查询：地址包含'杭州滨江'的所有用户列表")
    @Test
    void test_nested_query_address_like() {
        StudentQuery query = new StudentQuery()
            .where.id().in(HomeAddressQuery.class, q -> q
                .select.apply(studentId).end()
                .where.address().like("杭州滨江").end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .start("SELECT id, gmt_created, gmt_modified, is_deleted,")
            .end("FROM student " +
                "WHERE id IN (SELECT student_id FROM home_address WHERE address LIKE ?)");
    }
}
