package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static cn.org.atool.fluent.mybatis.segment.model.SqlOp.GT;

/**
 * GroupByTest
 *
 * @author darui.wu
 * @create 2020/6/20 9:33 下午
 */
public class GroupByTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_groupBy() throws Exception {
        UserQuery query = new UserQuery()
            .selectId()
            .where.id().eq(24L).end()
            .groupBy.apply("user_name", "age").end()
            .last("/** comment **/");
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE id = ? GROUP BY user_name, age /** comment **/");
    }

    @Test
    public void test_groupBy_having() throws Exception {
        UserQuery query = new UserQuery()
            .select("count(1)", "sum(1)")
            .where
            .id().eq(24L).end()
            .groupBy
            .userName().age().end()
            .having
            .apply("count(1)", GT, 2)
            .apply("sum(1) > ?", 3)
            .end();
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT count(1), sum(1) FROM t_user WHERE id = ? GROUP BY user_name, age HAVING count(1) > ? AND sum(1) > ?");
    }
}