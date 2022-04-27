package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

/**
 * SelectorTest
 *
 * @author darui.wu 2020/6/21 3:40 下午
 */
public class SelectorTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_select() {
        StudentQuery query = StudentQuery.emptyQuery();

        query.select
            .apply("id", "home_address_id", "1")
            .id()
            .max.age("max")
            .sum.age()
            .end()
            .where.id().eq(24L)
            .end()
            .groupBy.id()
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id`, `home_address_id`, 1, `id`, MAX(`age`) AS max, SUM(`age`) " +
                "FROM fluent_mybatis.student WHERE `id` = ? GROUP BY `id`", StringMode.SameAsSpace);
    }

    @Test
    public void test_select_alias() {
        StudentQuery query = StudentQuery.emptyQuery()
            .select
            .id("pk")
            .sum.age("sum")
            .max.age("max")
            .min.age("min")
            .avg.age("avg")
            .count.age("count")
            .group_concat.age("concat")
            .end()
            .where.id().eq(24L)
            .end()
            .groupBy.id()
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` AS pk, SUM(`age`) AS sum, MAX(`age`) AS max, MIN(`age`) AS min, AVG(`age`) AS avg, COUNT(`age`) AS count, GROUP_CONCAT(`age`) AS concat " +
                "FROM fluent_mybatis.student WHERE `id` = ? GROUP BY `id`");
    }

    @Test
    public void test_select_no_alias() {
        StudentQuery query = StudentQuery.emptyQuery()
            .selectId()
            .select
            .sum.age()
            .max.age()
            .min.age()
            .avg.age()
            .count.age()
            .group_concat.age()
            .end()
            .where
            .id().eq(24L)
            .end()
            .groupBy.id()
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id`, SUM(`age`), MAX(`age`), MIN(`age`), AVG(`age`), COUNT(`age`), GROUP_CONCAT(`age`) " +
                "FROM fluent_mybatis.student WHERE `id` = ? GROUP BY `id`");
    }

    @Test
    public void test_select2() {
        StudentQuery query = StudentQuery.emptyQuery()
            .selectId()
            .select.apply(f -> f.getProperty().startsWith("gmt")).end()
            .where.id().eq(24L).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id`, `gmt_created`, `gmt_modified` " +
                "FROM fluent_mybatis.student WHERE `id` = ?");
    }
}
