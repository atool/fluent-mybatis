package cn.org.atool.fluent.mybatis.join;

import cn.org.atool.fluent.mybatis.base.crud.JoinBuilder;
import cn.org.atool.fluent.mybatis.base.splice.FreeQuery;
import cn.org.atool.fluent.mybatis.generate.mapper.MemberMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.MemberQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;

public class FreeQueryAggregateTest extends BaseTest {
    @Autowired
    MemberMapper mapper;

    @Test
    void count() {
        FreeQuery query1 = new FreeQuery("t_member", "t1");
        query1.select("t1.id", "count(t1.gmt_modified)")
            .where.apply("id", EQ, "1").end()
            .groupBy.apply("t1.id").end()
        ;

        FreeQuery query2 = new FreeQuery("t_member", "t2", query1);
        query2.select("t2.id", "sum(t2.gmt_modified)")
            .where.apply("id", EQ, "1").end()
            .groupBy.apply("t2.id").end();

        JoinBuilder query = JoinBuilder
            .from(query1)
            .join(query2)
            .on("t1.id = t2.id");
        mapper.findOne(query.build());
        db.sqlList().wantFirstSql().eq("" +
                "SELECT t1.id, count(t1.gmt_modified), t2.id, sum(t2.gmt_modified) " +
                "FROM t_member t1 " +
                "JOIN t_member t2 " +
                "ON t1.id = t2.id " +
                "WHERE t1.id = ? AND t2.id = ? " +
                "GROUP BY t1.id, t2.id"
            , StringMode.SameAsSpace);
    }

    @Test
    void count_apply() {
        FreeQuery query1 = new FreeQuery("t_member", "t1");
        query1.select.apply("t1.id").count.apply("t1.gmt_modified").end()
            .where
            .applyIf(args -> args[0].equals("1"), "id", EQ, "1")
            .applyIf(args -> args[0].equals("1"), "id", EQ, "2")
            .end()
            .groupBy.apply("t1.id").end()
        ;

        FreeQuery query2 = new FreeQuery("t_member", "t2", query1);
        query2.select.apply("t2.id").sum.apply("t2.gmt_modified")
            .end()
            .where.apply("id", EQ, "1").end()
            .groupBy.apply("t2.id").end()
            .having.count.apply("id").gt(1).end();

        JoinBuilder query = JoinBuilder
            .from(query1)
            .join(query2)
            .on("t1.id = t2.id")
            ;
        mapper.findOne(query.build());
        db.sqlList().wantFirstSql().eq("" +
                "SELECT t1.id, COUNT(t1.gmt_modified), t2.id, SUM(t2.gmt_modified) " +
                "FROM t_member t1 " +
                "JOIN t_member t2 " +
                "ON t1.id = t2.id " +
                "WHERE t1.id = ? AND t2.id = ? " +
                "GROUP BY t1.id, t2.id " +
                "HAVING COUNT(t2.id) > ?"
            , StringMode.SameAsSpace);
    }

    @Test
    void joinNestedSelect(){
        FreeQuery query1 = new FreeQuery(new MemberQuery().where.age().gt(1).end(), "t1");
        query1.select.apply("t1.id").count.apply("t1.gmt_modified").end()
            .groupBy.apply("t1.id").end()
        ;

        FreeQuery query2 = new FreeQuery(new MemberQuery().groupBy.id().end(), "t2", query1);
        query2.select.apply("t2.id").sum.apply("t2.gmt_modified")
            .end()
            .where.apply("id", EQ, "1").end()
            .groupBy.apply("t2.id").end()
            .having.count.apply("id").gt(1).end();

        JoinBuilder query = JoinBuilder
            .from(query1)
            .join(query2)
            .on("t1.id = t2.id")
            ;
        mapper.findOne(query.build());
        db.sqlList().wantFirstSql().eq("" +
                "SELECT t1.id, COUNT(t1.gmt_modified), t2.id, SUM(t2.gmt_modified) " +
                "FROM (SELECT * FROM t_member WHERE age > ?) t1 " +
                "JOIN (SELECT * FROM t_member GROUP BY id) t2 " +
                "ON t1.id = t2.id " +
                "WHERE t2.id = ? " +
                "GROUP BY t1.id, t2.id " +
                "HAVING COUNT(t2.id) > ?"
            , StringMode.SameAsSpace);
    }
}