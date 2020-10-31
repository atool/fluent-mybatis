package cn.org.atool.fluent.mybatis.test.segment1;

import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SelectorTest
 *
 * @author darui.wu
 * @create 2020/6/21 3:40 下午
 */
public class SelectorTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_select() throws Exception {
        StudentQuery query = new StudentQuery();

        query.select
            .apply("id", "address_id", "1")
            .id()
            .max.age("max")
            .min.version()
            .sum.age()
            .end()
            .where.id().eq(24L)
            .end()
            .groupBy.id()
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id, address_id, 1, MAX(age) AS max, MIN(version), SUM(age) FROM t_student WHERE id = ? GROUP BY id");
    }

    @Test
    public void test_select_alias() throws Exception {
        StudentQuery query = new StudentQuery()
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
            .eq("SELECT id AS pk, SUM(age) AS sum, MAX(age) AS max, MIN(age) AS min, AVG(age) AS avg, COUNT(age) AS count, GROUP_CONCAT(age) AS concat " +
                "FROM t_student WHERE id = ? GROUP BY id");
    }

    @Test
    public void test_select_no_alias() throws Exception {
        StudentQuery query = new StudentQuery()
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
            .eq("SELECT id, SUM(age), MAX(age), MIN(age), AVG(age), COUNT(age), GROUP_CONCAT(age) " +
                "FROM t_student WHERE id = ? GROUP BY id");
    }

    @Test
    public void test_select2() throws Exception {
        StudentQuery query = new StudentQuery()
            .selectId()
            .select.apply(f -> f.getProperty().startsWith("gmt")).end()
            .where.id().eq(24L).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id, gmt_created, gmt_modified FROM t_student WHERE id = ?");
    }
}
