package cn.org.atool.fluent.dbtest;

import cn.org.atool.fluent.mybatis.db.sqlite.mapper.SqliteStudentMapper;
import cn.org.atool.fluent.mybatis.db.sqlite.wrapper.SqliteStudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.sql.SQLSyntaxErrorException;

public class SqliteTest extends BaseTest {
    @Autowired
    SqliteStudentMapper mapper;

    @DisplayName("验证Sqlite数据库分页")
    @Test
    void testPage() {
        want.exception(() ->
                new SqliteStudentQuery().selectId().limit(1, 10).of(mapper).listEntity()
            , SQLSyntaxErrorException.class, BadSqlGrammarException.class);

        db.sqlList().wantFirstSql().eq("" +
            "SELECT \"id\" FROM \"student\" LIMIT ? OFFSET ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(10, 1);
    }
}