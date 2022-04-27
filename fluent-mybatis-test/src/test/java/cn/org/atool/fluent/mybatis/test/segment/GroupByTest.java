package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * GroupByTest
 *
 * @author darui.wu 2020/6/20 9:33 下午
 */
public class GroupByTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_groupBy() {
        StudentQuery query = StudentQuery.emptyQuery()
            .selectId()
            .where.id().eq(24L).end()
            .groupBy.userName().age().end()
            .last("/** comment **/");
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` FROM fluent_mybatis.student " +
                "WHERE `id` = ? GROUP BY `user_name`, `age` /** comment **/");
    }

    @Test
    public void test_groupBy2() {
        StudentQuery query = StudentQuery.emptyQuery()
            .selectId()
            .where.id().eq(24L).end()
            .groupBy.apply(Ref.Field.Student.userName, Ref.Field.Student.age).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` FROM fluent_mybatis.student " +
                "WHERE `id` = ? GROUP BY `user_name`, `age`");
    }

    @Test
    public void test_groupBy_condition() {
        StudentQuery query = StudentQuery.emptyQuery()
            .selectId()
            .where.id().eq(24L).end()
            .groupBy
            .apply(true, Ref.Field.Student.userName)
            .apply(false, Ref.Field.Student.age).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` FROM fluent_mybatis.student WHERE `id` = ? GROUP BY `user_name`");
    }

    @Test
    public void test_groupBy_having() {
        StudentQuery query = StudentQuery.emptyQuery()
            .select.apply("count(1)", "sum(1)")
            .end()
            .where.id().eq(24L)
            .end()
            .groupBy.userName().age().end()
            .having.apply("count(1)").gt(10)
            .and.sum.age().gt(3)
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT count(1), sum(1) FROM fluent_mybatis.student " +
                "WHERE `id` = ? GROUP BY `user_name`, `age` " +
                "HAVING count(1) > ? AND SUM(`age`) > ?");
    }

    @DisplayName("按级别grade统计年龄在15和25之间的人数在10人以上，该条件内最大、最小和平均年龄")
    @Test
    public void test_count_gt_10_groupByGrade() {
        StudentQuery query = StudentQuery.emptyQuery()
            .select
            .apply(Ref.Field.Student.grade.column)
            .count.id()
            .max.age()
            .min.age()
            .avg.age()
            .end()
            .where.age().between(15, 25)
            .end()
            .groupBy.grade()
            .end()
            .having.count.id().gt(10)
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `grade`, COUNT(`id`), MAX(`age`), MIN(`age`), AVG(`age`) " +
                "FROM fluent_mybatis.student " +
                "WHERE `age` BETWEEN ? AND ? " +
                "GROUP BY `grade` " +
                "HAVING COUNT(`id`) > ?");
    }
}
