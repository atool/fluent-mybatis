package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.condition.model.SqlOp;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
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
            .select(by -> by.id.select().age.sum("avg"))
            .and.id.eq(24L)
            .groupBy(by -> by.id.apply())
            .having(by -> by
                .age.sum(SqlOp.BETWEEN, 2, 10)
                .apply("avg > ?", 10)
            );
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id, SUM(age) AS avg FROM t_user WHERE id = ? " +
                "GROUP BY id HAVING SUM(age) BETWEEN ? AND ? AND avg > ?");
    }
}