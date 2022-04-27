package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentScoreMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentScoreQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AggregateTest extends BaseTest {
    @Autowired
    StudentScoreMapper mapper;

    @Test
    void aggregate() {
        StudentScoreQuery query = StudentScoreQuery.emptyQuery()
            .select.schoolTerm().subject()
            .count("count")
            .min.score()
            .max.score("max_score")
            .avg.score("avg_score")
            .sum.score()
            .group_concat.score()
            .end()
            .groupBy.schoolTerm().subject().end();

        mapper.listMaps(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT `school_term`, " +
            "`subject`, " +
            "COUNT(*) AS count, " +
            "MIN(`score`), " +
            "MAX(`score`) AS max_score, " +
            "AVG(`score`) AS avg_score, " +
            "SUM(`score`), " +
            "GROUP_CONCAT(`score`) " +
            "FROM `student_score` " +
            "GROUP BY `school_term`, `subject`");
    }

    @Test
    void select_apply() {
        StudentScoreQuery query = StudentScoreQuery.emptyQuery()
            .select.apply("school_term").subject("MySubject")
            .apply("min(score) as min_score", "group_concat(id order by id desc separator ';')")
            .end()
            .groupBy.schoolTerm().subject().end();

        mapper.listMaps(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT `school_term`, `subject` AS MySubject, " +
            "min(score) as min_score, " +
            "group_concat(id order by id desc separator ';') " +
            "FROM `student_score` " +
            "GROUP BY `school_term`, `subject`");
    }
}
