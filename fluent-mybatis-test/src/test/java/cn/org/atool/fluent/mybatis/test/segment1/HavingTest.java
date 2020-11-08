package cn.org.atool.fluent.mybatis.test.segment1;

import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static cn.org.atool.fluent.mybatis.generate.helper.StudentMapping.id;

/**
 * HavingTest
 *
 * @author darui.wu
 * @create 2020/6/21 6:50 下午
 */
public class HavingTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_groupBy_having() throws Exception {
        StudentQuery query = new StudentQuery()
            .select
            .sum.age("avg")
            .apply(id.column)
            .end()
            .where.id().eq(24L).end()
            .groupBy.id().end()
            .having.sum.age().between(2, 10)
            .and.count.id().gt(2)
            .and.avg.age().in(new int[]{2, 3})
            .and.min.age().gt(10)
            .and.apply("avg").gt(10)
            .and.max.age().lt(20)
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT SUM(age) AS avg, id FROM student WHERE id = ? " +
                "GROUP BY id " +
                "HAVING SUM(age) BETWEEN ? AND ? " +
                "AND COUNT(id) > ? " +
                "AND AVG(age) IN (?, ?) " +
                "AND MIN(age) > ? " +
                "AND avg > ? " +
                "AND MAX(age) < ?");
    }

    @Test
    public void test_groupBy_having2() throws Exception {
        StudentQuery query = new StudentQuery()
            .select
            .sum.age("avg")
            .apply(id.column)
            .end()
            .where.id().eq(24L)
            .end()
            .groupBy.id()
            .end()
            .having.max.age().lt(20)
            .and.apply("SUM(age)").lt(20)
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT SUM(age) AS avg, id FROM student WHERE id = ? " +
                "GROUP BY id " +
                "HAVING MAX(age) < ? AND SUM(age) < ?");
    }
}
