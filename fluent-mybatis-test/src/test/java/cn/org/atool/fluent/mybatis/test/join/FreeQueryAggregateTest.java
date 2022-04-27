package cn.org.atool.fluent.mybatis.test.join;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.JoinBuilder;
import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.generator.shared3.mapper.MemberMapper;
import cn.org.atool.fluent.mybatis.generator.shared3.wrapper.MemberQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;

@SuppressWarnings("rawtypes")
public class FreeQueryAggregateTest extends BaseTest {
    @Autowired
    MemberMapper mapper;

    @Test
    void count() {
        IQuery query = new FreeQuery("t_member", "t1")
            .select("t1.id", "count(t1.age)")
            .where.apply("id", EQ, "1").end()
            .groupBy.apply("t1.id").end()
            .join(new FreeQuery("t_member", "t2")
                .select("t2.id", "sum(t2.age)")
                .where.apply("id", EQ, "1").end()
                .groupBy.apply("t2.id").end()
            )
            .onApply("t1.id = t2.id").endJoin()
            .build();
        mapper.findOne(query);
        db.sqlList().wantFirstSql().eq("" +
                "SELECT t1.id, count(t1.age), t2.id, sum(t2.age) " +
                "FROM `t_member` t1 " +
                "JOIN `t_member` t2 " +
                "ON t1.id = t2.id " +
                "WHERE t1.`id` = ? AND t2.`id` = ? " +
                "GROUP BY t1.id, t2.id"
            , StringMode.SameAsSpace);
    }

    @Test
    void count_apply() {
        FreeQuery query1 = new FreeQuery("t_member", "t1");
        query1.select.apply("t1.id").count.apply("t1.age").end()
            .where
            .applyIf(args -> args[0].equals("1"), "id", EQ, "1")
            .applyIf(args -> args[0].equals("1"), "id", EQ, "2")
            .end()
            .groupBy.apply("t1.id").end()
        ;

        FreeQuery query2 = new FreeQuery("t_member", "t2");
        query2.select.apply("t2.id").sum.apply("t2.age")
            .end()
            .where.apply("id", EQ, "1").end()
            .groupBy.apply("t2.id").end()
            .having.count.apply("id").gt(1).end();

        JoinBuilder query = JoinBuilder
            .from(query1)
            .join(query2)
            .onApply("t1.id = t2.id").endJoin();
        mapper.findOne(query.build());
        db.sqlList().wantFirstSql().eq("" +
                "SELECT t1.id, COUNT(t1.age), t2.id, SUM(t2.age) " +
                "FROM `t_member` t1 " +
                "JOIN `t_member` t2 " +
                "ON t1.id = t2.id " +
                "WHERE t1.`id` = ? AND t2.`id` = ? " +
                "GROUP BY t1.id, t2.id " +
                "HAVING COUNT(t2.`id`) > ?"
            , StringMode.SameAsSpace);
    }

    @Test
    void joinNestedSelect() {
        IQuery query = new FreeQuery(new MemberQuery().where.age().gt(1).end(), "t1")
            .select.apply("id").count.apply("age").end()
            .groupBy.apply("id").end()
            .join(
                new FreeQuery(new MemberQuery().groupBy.id().end(), "t2")
                    .select.apply("id").sum.apply("age").end()
                    .where.apply("id", EQ, "1").end()
                    .groupBy.apply("id").end()
                    .having.count.apply("id").gt(1).end())
            .onApply("t1.id = t2.id").endJoin()
            .build();
        mapper.findOne(query);
        db.sqlList().wantFirstSql().containsInOrder(
            "SELECT t1.`id`, COUNT(t1.`age`), t2.`id`, SUM(t2.`age`) ",
            "FROM (SELECT `id`, ",
            "` FROM `t_member` WHERE `age` > ?) t1 JOIN (SELECT `id`,",
            "` FROM `t_member` GROUP BY `id`) t2 " +
                "ON t1.id = t2.id " +
                "WHERE t2.`id` = ? " +
                "GROUP BY t1.`id`, t2.`id` " +
                "HAVING COUNT(t2.`id`) > ?"
        );
    }
}
