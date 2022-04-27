package cn.org.atool.fluent.mybatis.test.free;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.JoinBuilder;
import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.generator.shared3.Ref;
import cn.org.atool.fluent.mybatis.generator.shared3.mapper.MemberMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.test4j.hamcrest.matcher.string.StringMode;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;

@SuppressWarnings({"rawtypes"})
public class FreeQuery2Test extends BaseTest {
    @Autowired
    MemberMapper mapper;

    @Test
    void test() {
        FreeQuery query = new FreeQuery("t_member")
            .select("id", "gmt_modified")
            .where.apply("id", EQ, "1").end()
            .groupBy.apply("id").end();
        mapper.findOne(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT `id`, `gmt_modified` FROM `t_member` WHERE `id` = ? GROUP BY `id`");
    }

    @Test
    void test_alias() {
        FreeQuery query = new FreeQuery("t_member", "t1")
            .select.apply("id")
            .applyAs(Ref.Field.Member.gmtModified, "modifiedDate").end()
            .where().apply("id", EQ, "1").end();
        mapper.findOne(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT t1.`id`, t1.`gmt_modified` AS modifiedDate " +
            "FROM `t_member` t1 WHERE t1.`id` = ?");
    }

    @Test
    void test_sum_alias() {
        FreeQuery query = new FreeQuery("t_member", "t1");
        query
            .select.apply("id")
            .max.applyAs(Ref.Field.Member.age, "_age").end()
            .where().apply("id", EQ, "1").end()
            .groupBy.apply("id").end();
        mapper.findOne(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT t1.`id`, MAX(t1.`age`) AS _age " +
            "FROM `t_member` t1 WHERE t1.`id` = ? " +
            "GROUP BY t1.`id`");
    }

    @Test
    void test_join() {
        FreeQuery query1 = new FreeQuery("t_member", "t1");
        query1
            .select("t1.id", "t1.gmt_modified")
            .where.apply("id", EQ, "1").end();

        FreeQuery query2 = new FreeQuery("t_member", "t2");
        query2
            .select("t2.id", "t2.gmt_modified")
            .where.apply("id", EQ, "1").end();

        JoinBuilder query = JoinBuilder
            .from(query1)
            .join(query2)
            .onApply("t1.id = t2.id").endJoin()
            .limit(20);

        mapper.findOne(query.build());
    }

    @DisplayName("三表关联")
    @Test
    void test_join3() {
        FreeQuery query1 = new FreeQuery("t_member", "t1");
        query1
            .select("t1.id", "t1.gmt_modified")
            .where.apply("id", EQ, "1").end();

        FreeQuery query2 = new FreeQuery("t_member", "t2");
        query2
            .select("t2.id", "t2.gmt_modified")
            .where.apply("id", EQ, "1").end();

        FreeQuery query3 = new FreeQuery("t_member2", "t3");
        query3
            .select("t3.id", "t3.gmt_modified")
            .where.apply("id", EQ, "1").end();

        JoinBuilder query = JoinBuilder
            .from(query1)
            .join(query2)
            .onApply("t1.id = t2.id").endJoin()
            .leftJoin(query3)
            .onApply("t1.id=t3.id").endJoin()
            .limit(20);

        try {
            mapper.findOne(query.distinct().build());
            want.fail();
        } catch (Exception e) {
            db.sqlList().wantFirstSql().eq("" +
                "SELECT DISTINCT t1.id, t1.gmt_modified, t2.id, t2.gmt_modified, t3.id, t3.gmt_modified " +
                "FROM  `t_member` t1 JOIN `t_member` t2 " +
                "ON t1.id = t2.id " +
                "LEFT JOIN `t_member2` t3 " +
                "ON t1.id=t3.id  " +
                "WHERE t1.`id` = ?  " +
                "AND  t2.`id` = ?  " +
                "AND  t3.`id` = ? " +
                "LIMIT ?, ?", StringMode.SameAsSpace);
        }
    }

    IQuery buildFreeJoin(QueryModel bean) {
        FreeQuery query1 = new FreeQuery(bean.table1, "t1");
        query1.select.avg(bean.t1column1, "value").end()
            .where.apply(bean.t1column2, GE, "2020-11-01 00:00:00")
            .and.apply("gmt_create", LE, "2020-12-01 23:59:59").end();

        FreeQuery query2 = new FreeQuery(bean.table2, "t2");
        query2
            .select.applyAs("product_id", "productId").end()
            .where
            .apply("super_id", EQ, "153264")
            .end();

        JoinBuilder builder = JoinBuilder
            .from(query1)
            .join(query2)
            .onApply("t1.product_id = t2.product_id").endJoin();
        return builder.build();
    }

    @Test
    void test_join2() {
        IQuery query = buildFreeJoin(new QueryModel()
            .setTable1("dwd_metric_bug_df")
            .setT1column1("close_duration")
            .setT1column2("gmt_closed")
            .setTable2("dim_metric_product_super_df"));
        want.exception(() -> mapper.findOne(query), DataAccessException.class);
        db.sqlList().wantFirstSql().eq("" +
                "SELECT AVG(t1.`close_duration`) AS value, t2.`product_id` AS productId " +
                "FROM  `dwd_metric_bug_df` t1 " +
                "JOIN `dim_metric_product_super_df` t2 " +
                "ON t1.product_id = t2.product_id " +
                "WHERE t1.`gmt_closed` >= ? " +
                "AND  t1.`gmt_create` <= ? " +
                "AND  t2.`super_id` = ?"
            , StringMode.SameAsSpace);
    }

    @Setter
    @Accessors(chain = true)
    public static class QueryModel {
        String table1;

        String t1column1;

        String t1column2;

        String table2;
    }
}
