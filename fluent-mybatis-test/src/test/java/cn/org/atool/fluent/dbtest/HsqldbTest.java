package cn.org.atool.fluent.dbtest;

import cn.org.atool.fluent.mybatis.db.hsql.mapper.HSqlStudentMapper;
import cn.org.atool.fluent.mybatis.db.hsql.wrapper.HSqlStudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.sql.SQLSyntaxErrorException;

public class HsqldbTest extends BaseTest {
    @Autowired
    HSqlStudentMapper mapper;

    @DisplayName("验证hsqldb数据库分页")
    @Test
    void testPage() {
        want.exception(() ->
                new HSqlStudentQuery().selectId().limit(1, 10).of(mapper).listEntity()
            , SQLSyntaxErrorException.class, BadSqlGrammarException.class);

        db.sqlList().wantFirstSql().eq("" +
            "SELECT ID FROM  STUDENT OFFSET ? ROWS LIMIT ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(1, 10);
    }
}