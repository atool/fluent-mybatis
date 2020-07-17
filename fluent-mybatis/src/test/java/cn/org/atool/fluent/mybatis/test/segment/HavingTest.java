package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.demo.generate.helper.UserMapping;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static cn.org.atool.fluent.mybatis.demo.generate.helper.UserMapping.id;

/**
 * HavingTest
 *
 * @author darui.wu
 * @create 2020/6/21 6:50 下午
 */
public class HavingTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_groupBy_having() throws Exception {
        UserQuery query = new UserQuery()
            .select
            .sum.age("avg")
            .apply(id.column)
            .end()
            .where.id().eq(24L).end()
            .groupBy.id()
            .end()
            .having
            .sum.age().between(2, 10)
            .count.id().gt(2)
            .avg.age().in(new int[]{2, 3})
            .min.age().gt(10)
            .apply("avg").gt(10)
            .max.age().lt(20)
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT SUM(age) AS avg, id FROM t_user WHERE id = ? " +
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
        UserQuery query = new UserQuery()
            .select
            .sum.age("avg")
            .apply(id.column)
            .end()
            .where.id().eq(24L).end()
            .groupBy.id()
            .end()
            .having
            .max.age().lt(20)
            .apply("SUM(age)").lt(20)
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT SUM(age) AS avg, id FROM t_user WHERE id = ? " +
                "GROUP BY id " +
                "HAVING MAX(age) < ? AND SUM(age) < ?");
    }
}