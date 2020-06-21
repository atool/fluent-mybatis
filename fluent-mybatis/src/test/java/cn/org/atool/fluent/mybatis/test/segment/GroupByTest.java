package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static cn.org.atool.fluent.mybatis.condition.model.SqlOp.GT;

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
            .and.id.eq(24L)
            .groupBy(by -> by.apply("user_name", "age"))
            .last("/** comment **/");
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE id = ? GROUP BY user_name, age /** comment **/");
    }

    @Test
    public void test_groupBy_having() throws Exception {
        UserQuery query = new UserQuery()
            .select("count(1)", "sum(1)")
            .and.id.eq(24L)
            .groupBy(by -> by.userName.apply().age.apply())
            .having(by -> by
                .apply("count(1)", GT, 2)
                .apply("sum(1) > ?", 3)
            );
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT count(1), sum(1) FROM t_user WHERE id = ? GROUP BY user_name, age HAVING count(1) > ? AND sum(1) > ?");
    }
}