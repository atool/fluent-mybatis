package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
            .select(by -> by
                .id().as()
                .age().sum("avg"))
            .where.id().eq(24L).end()
            .groupBy.id()
            .end()
            .having
            .age().sum().between(2, 10)
            .id().count().gt(2)
            .age().avg().in(2, 3)
            .age().min().gt(10)
            .apply("avg").gt(10)
            .age().max().lt(20)
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id, SUM(age) AS avg FROM t_user WHERE id = ? " +
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
            .select(by -> by
                .id().as()
                .age().sum("avg"))
            .where.id().eq(24L).end()
            .groupBy.id()
            .end()
            .having
            .age().max().lt(20)
            .age().apply(c -> String.format("SUM(%s)", c)).lt(20)
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id, SUM(age) AS avg FROM t_user WHERE id = ? " +
                "GROUP BY id " +
                "HAVING MAX(age) < ? AND SUM(age) < ?");
    }
}