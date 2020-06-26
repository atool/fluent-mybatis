package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
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
    private UserMapper mapper;

    @Test
    public void test_select() throws Exception {
        UserQuery query = new UserQuery()
            .select(selector -> selector
                .id().as()
                .age().sum()
                .apply("address_id", "1"))
            .where.id().eq(24L).end()
            .groupBy.id().end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id, SUM(age), address_id, 1 FROM t_user WHERE id = ? GROUP BY id");
    }

    @Test
    public void test_select_alias() throws Exception {
        UserQuery query = new UserQuery()
            .select(selector -> selector
                .id().as("pk")
                .age().sum("sum")
                .age().max("max")
                .age().min("min")
                .age().avg("avg")
                .age().count("count")
                .age().group_concat("concat")
            )
            .where.id().eq(24L).end()
            .groupBy.id().end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id AS pk, SUM(age) AS sum, MAX(age) AS max, MIN(age) AS min, AVG(age) AS avg, COUNT(age) AS count, GROUP_CONCAT(age) AS concat " +
                "FROM t_user WHERE id = ? GROUP BY id");
    }

    @Test
    public void test_select_no_alias() throws Exception {
        UserQuery query = new UserQuery()
            .select(selector -> selector
                .id().as()
                .age().sum()
                .age().max()
                .age().min()
                .age().avg()
                .age().count()
                .age().group_concat())
            .where.id().eq(24L).end()
            .groupBy.id().end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id, SUM(age), MAX(age), MIN(age), AVG(age), COUNT(age), GROUP_CONCAT(age) " +
                "FROM t_user WHERE id = ? GROUP BY id");
    }

    @Test
    public void test_select2() throws Exception {
        UserQuery query = new UserQuery()
            .select(selector -> selector.apply(f -> f.getProperty().startsWith("gmt")))
            .where.id().eq(24L).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT gmt_created, gmt_modified FROM t_user WHERE id = ?");
    }
}