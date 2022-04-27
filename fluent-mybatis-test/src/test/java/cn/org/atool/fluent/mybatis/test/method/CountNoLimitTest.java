package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author darui.wu 2019/10/29 9:33 下午
 */
public class CountNoLimitTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void count_distinct() {
        StudentQuery.emptyQuery().select("count(distinct id)").to().count();
        db.sqlList().wantFirstSql().eq("" +
            "SELECT count(distinct id) FROM fluent_mybatis.student");
    }

    @Test
    void count_distinct2() {
        StudentQuery.emptyQuery().select("distinct id").to().count();
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(DISTINCT id) FROM fluent_mybatis.student");
    }

    @Test
    void count_distinct3() {
        StudentQuery.emptyQuery().distinct().select.id().end().to().count();
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(DISTINCT `id`) FROM fluent_mybatis.student");
    }

    @Test
    void count_distinct4() {
        StudentQuery.emptyQuery().select.id().end().to().count();
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(`id`) FROM fluent_mybatis.student");
    }

    @Test
    public void test_count_no_limit() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().eq(24L).end()
            .groupBy.userName().end()
            .orderBy.userName().asc().end()
            .limit(10);

        mapper.count(query);
        db.sqlList().wantFirstSql()
            .start("" +
                "SELECT COUNT(*) " +
                "FROM (SELECT COUNT(*) " +
                "FROM fluent_mybatis.student WHERE `id` = ? " +
                "GROUP BY `user_name` " +
                "LIMIT ?, ?) TMP_");

        mapper.countNoLimit(query);
        db.sqlList().wantSql(1).start("" +
            "SELECT COUNT(*) " +
            "FROM (SELECT COUNT(*) " +
            "FROM fluent_mybatis.student " +
            "WHERE `id` = ? " +
            "GROUP BY `user_name`) TMP_");
    }

    @Test
    public void test_count_and_list() {
        ATM.dataMap.student.initTable(100)
            .age.values(10)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().eq(10)
            .end()
            .orderBy.userName().asc()
            .end()
            .limit(10, 20);
        int count = mapper.countNoLimit(query);
        db.sqlList().wantFirstSql()
            .start("SELECT COUNT(*)")
            .end("FROM fluent_mybatis.student WHERE `age` = ?");
        want.number(count).eq(100);

        List<StudentEntity> list = mapper.listEntity(query);
        db.sqlList().wantSql(1)
            .start("SELECT `id`,")
            .end(", `gmt_created`, `gmt_modified`, `is_deleted` " +
                "FROM fluent_mybatis.student " +
                "WHERE `age` = ? " +
                "ORDER BY `user_name` ASC LIMIT ?, ?");
        want.list(list).sizeEq(20);
    }
}
