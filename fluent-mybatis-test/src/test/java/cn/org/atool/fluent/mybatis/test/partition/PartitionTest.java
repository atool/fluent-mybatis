package cn.org.atool.fluent.mybatis.test.partition;

import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentUpdate;
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
        String userName = "my_test_name";
        // 编码实现分表逻辑
        StringSupplier table = () -> "student_" + userName.hashCode() % 2;
        StudentQuery query = StudentQuery.emptyQuery(table)
            .where.defaults()
            .and.userName().eq(userName).end();
        want.exception(() -> mapper.listEntity(query),
            SQLSyntaxErrorException.class, BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().end("" +
            "FROM  `student_1` WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(false, "test_env", "my_test_name");
    }

    @Test
    void test_update_partition() {
        String userName = "my_test_name";
        // 编码实现分表逻辑
        StringSupplier table = () -> "student_" + userName.hashCode() % 2;
        StudentUpdate updater = StudentUpdate.emptyUpdater(table)
            .set.userName().is("test").end()
            .where.defaults()
            .and.userName().eq(userName).end();
        want.exception(() -> mapper.updateBy(updater),
            SQLSyntaxErrorException.class, BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE `student_1` " +
            "SET `gmt_modified` = now(), `user_name` = ? " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList("test", false, "test_env", "my_test_name");
    }
}
