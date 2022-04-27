package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * HavingTest
 *
 * @author darui.wu 2020/6/21 6:50 下午
 */
public class HavingTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_groupBy_having_query() {
        StudentQuery query = StudentQuery.emptyQuery()
            .select.gender().sum.age("avg").end()
            .where.id().gt(24L).end()
            .groupBy.gender().end()
            .having.avg.age().apply(SqlOp.GT, StudentQuery.emptyQuery().select.age().end().where.id().eq(34).end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT `gender`, SUM(`age`) AS avg " +
            "FROM fluent_mybatis.student WHERE `id` > ? " +
            "GROUP BY `gender` HAVING AVG(`age`) > (SELECT `age` FROM fluent_mybatis.student WHERE `id` = ?)");
        db.sqlList().wantFirstPara().eqList(24L, 34);
    }

    @Test
    public void test_groupBy_having_applyFun() {
        StudentQuery query = StudentQuery.emptyQuery()
            .select.gender().sum.age("avg").end()
            .where.id().gt(24L).end()
            .groupBy.gender().end()
            .having.avg.age().applyFunc(SqlOp.GT, "(? + ?)", 12, 23)
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT `gender`, SUM(`age`) AS avg " +
            "FROM fluent_mybatis.student WHERE `id` > ? " +
            "GROUP BY `gender` HAVING AVG(`age`) > (? + ?)");
        db.sqlList().wantFirstPara().eqList(24L, 12, 23);
    }

    @Test
    public void test_groupBy_having() {
        StudentQuery query = StudentQuery.emptyQuery()
            .select.gender().sum.age("avg").end()
            .where.id().gt(24L).end()
            .groupBy.gender().end()
            .having.sum.age().between(2, 10)
            .and.count.id().gt(2)
            .and.avg.age().in(new int[]{2, 3})
            .and.min.age().gt(10)
            .and.apply("avg").gt(10)
            .and.max.age().lt(20)
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `gender`, SUM(`age`) AS avg " +
                "FROM fluent_mybatis.student WHERE `id` > ? " +
                "GROUP BY `gender` " +
                "HAVING SUM(`age`) BETWEEN ? AND ? " +
                "AND COUNT(`id`) > ? " +
                "AND AVG(`age`) IN (?, ?) " +
                "AND MIN(`age`) > ? " +
                "AND avg > ? " +
                "AND MAX(`age`) < ?");
    }

    @Test
    public void test_groupBy_having2() {
        StudentQuery query = StudentQuery.emptyQuery()
            .select
            .sum.age("avg")
            .apply(Ref.Field.Student.id.column)
            .end()
            .where.id().eq(24L)
            .end()
            .groupBy.id()
            .end()
            .having.max.age().lt(20)
            .and.apply("SUM(age)").lt(20)
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT SUM(`age`) AS avg, `id` FROM fluent_mybatis.student WHERE `id` = ? " +
                "GROUP BY `id` " +
                "HAVING MAX(`age`) < ? AND SUM(age) < ?");
    }
}
