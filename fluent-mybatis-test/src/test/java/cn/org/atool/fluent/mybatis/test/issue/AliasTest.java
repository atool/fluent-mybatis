package cn.org.atool.fluent.mybatis.test.issue;

import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AliasTest extends BaseTest {
    @Autowired
    StudentMapper mapper;

    @Test
    void issue_I3V91T_1() {
        StudentQuery query = StudentQuery.emptyQuery("t")
            .select.userName("u").end()
            .orderBy.desc("u").end();
        mapper.listObjs(query);
        db.sqlList().wantFirstSql().eq("SELECT t.`user_name` AS u FROM fluent_mybatis.student t ORDER BY u DESC");
    }

    @Test
    void issue_I3V91T_2() {
        StudentQuery query = StudentQuery.emptyQuery("t")
            .select.userName("u").sum.age("a").end()
            .groupBy.apply("u").end()
            .having.apply("a").gt(30).end();
        mapper.listObjs(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT t.`user_name` AS u, SUM(t.`age`) AS a " +
            "FROM fluent_mybatis.student t " +
            "GROUP BY u " +
            "HAVING a > ?");
    }

    @Test
    void testSelectAll() {
        FreeQuery query = new FreeQuery(StudentQuery.emptyQuery().where.age().gt(30).end(), "aa")
            .select.apply("*").end();
        mapper.listMaps(query);
        db.sqlList().wantFirstSql().containsInOrder(
            "SELECT * FROM (SELECT `",
            "` FROM fluent_mybatis.student WHERE `age` > ?) aa");
    }
}
