package cn.org.atool.fluent.mybatis.test.nested;

import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.List;

import static cn.org.atool.fluent.mybatis.generate.helper.StudentMapping.id;

/**
 * InNestQueryTest
 *
 * @author darui.wu
 * @create 2020/6/19 10:54 下午
 */
public class InNestQueryTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void test_and_in_nested3() {
        StudentQuery query = new StudentQuery()
            .select.id().age().end()
            .where.apply("(id, age)").in(q -> q.select.id().age().end()
                .where.id().eq(3L).end())
            .and.userName().like("user")
            .and.age().gt(23).end();

        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id`, `age` " +
                "FROM student " +
                "WHERE (id, age) IN (SELECT `id`, `age` FROM student WHERE `id` = ?) " +
                "AND `user_name` LIKE ? " +
                "AND `age` > ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(3L, "%user%", 23);
    }

    @Test
    void test_and_in_nested() {
        StudentQuery query = new StudentQuery()
            .select.apply(id).sum.age().end()
            .where.id().in(q -> q.selectId()
                .where.id().eq(3L).end())
            .and.userName().like("user")
            .and.age().gt(23).end()
            .groupBy.id().end()
            .having.sum.age().gt(2)
            .and.sum.age().le(4).end();

        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id`, SUM(`age`) FROM student " +
                "WHERE `id` IN (SELECT `id` FROM student WHERE `id` = ?) " +
                "AND `user_name` LIKE ? " +
                "AND `age` > ? " +
                "GROUP BY `id` " +
                "HAVING SUM(`age`) > ? AND SUM(`age`) <= ?", StringMode.SameAsSpace);
    }

    @Test
    void test_and_in_nested_1() {
        StudentQuery query = new StudentQuery()
            .where.id().in(q -> q.selectId()
                .where.id().eq(3L).end())
            .and.userName().like("user").end();

        List list = mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .end("WHERE `id` IN (SELECT `id` FROM student WHERE `id` = ?) " +
                "AND `user_name` LIKE ?");
    }

    @Test
    void test_and_in_nested2() {
        StudentQuery query = new StudentQuery()
            .selectId()
            .where.homeAddressId().in(new HomeAddressQuery().selectId()
                .where.id().in(new int[]{1, 2}).end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` FROM student " +
                "WHERE `home_address_id` IN (SELECT `id` " +
                "   FROM home_address WHERE `id` IN (?, ?))", StringMode.SameAsSpace);
    }
}
