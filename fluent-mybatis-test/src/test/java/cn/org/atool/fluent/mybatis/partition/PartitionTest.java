package cn.org.atool.fluent.mybatis.partition;

import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.sql.SQLSyntaxErrorException;

public class PartitionTest extends BaseTest {
    @Autowired
    StudentMapper mapper;

    @Test
    void test_query_partition() {
        StudentQuery query = StudentQuery.query(() -> "student_001")
            .defaultWhere()
            .id().eq(1).end();
        want.exception(() -> mapper.listEntity(query),
            SQLSyntaxErrorException.class, BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().end("" +
            "FROM  student_001 WHERE `is_deleted` = ? AND `env` = ? AND `id` = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(false, "test_env", 1);
    }

    @Test
    void test_update_partition() {
        StudentUpdate updater = StudentUpdate.updater(() -> "student_001")
            .set.userName().is("test").end()
            .defaultWhere()
            .id().eq(1).end();
        want.exception(() -> mapper.updateBy(updater),
            SQLSyntaxErrorException.class, BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE student_001 " +
            "SET `gmt_modified` = now(), `user_name` = ? " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `id` = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList("test", false, "test_env", 1);
    }
}
