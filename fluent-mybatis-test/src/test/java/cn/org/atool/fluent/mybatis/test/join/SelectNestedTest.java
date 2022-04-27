package cn.org.atool.fluent.mybatis.test.join;

import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.generator.shared3.mapper.MemberMapper;
import cn.org.atool.fluent.mybatis.generator.shared3.wrapper.MemberQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;

public class SelectNestedTest extends BaseTest {
    @Autowired
    MemberMapper mapper;

    @DisplayName("select子查询")
    @Test
    void test_select_nested() {
        MemberQuery child = new MemberQuery()
            .select.id().count.gmtModified("_count").end()
            .where.id().eq(1).end()
            .groupBy.id().end();

        FreeQuery query = new FreeQuery(child, "t2");
        query.select("t2.id", "t2._count")
            .where.apply("id", EQ, "1").end()
            .groupBy.apply("t2.id").end();

        mapper.findOne(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT t2.id, t2._count " +
            "FROM (SELECT `id`, COUNT(`gmt_modified`) AS _count " +
            "   FROM `t_member` WHERE `id` = ? GROUP BY `id`) t2 " +
            "WHERE t2.`id` = ? GROUP BY t2.id", StringMode.SameAsSpace);
    }

    @DisplayName("select子查询2")
    @Test
    void test_select_nested2() {
        FreeQuery child = new FreeQuery("t_member", "t1");
        child
            .select("t1.id", "count(t1.gmt_modified) as _count")
            .where.apply("id", EQ, "1").end()
            .groupBy.apply("t1.id").end();

        FreeQuery query2 = new FreeQuery(child, "t2");
        query2
            .select("t2.id", "t2._count")
            .where.apply("id", EQ, "1").end()
            .groupBy.apply("t2.id").end();

        mapper.findOne(query2);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT t2.id, t2._count " +
            "FROM (SELECT t1.id, count(t1.gmt_modified) as _count " +
            "   FROM `t_member` t1 WHERE t1.`id` = ? GROUP BY t1.id) t2 " +
            "WHERE t2.`id` = ? GROUP BY t2.id", StringMode.SameAsSpace);
    }
}
