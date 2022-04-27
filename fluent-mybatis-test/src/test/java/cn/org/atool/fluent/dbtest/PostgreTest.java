package cn.org.atool.fluent.dbtest;

import cn.org.atool.fluent.mybatis.db.pg.entity.PgStudentEntity;
import cn.org.atool.fluent.mybatis.db.pg.mapper.PgStudentMapper;
import cn.org.atool.fluent.mybatis.db.pg.wrapper.PgStudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.sql.SQLSyntaxErrorException;

public class PostgreTest extends BaseTest {
    @Autowired
    PgStudentMapper mapper;

    @DisplayName("验证postgre数据库分页")
    @Test
    void testPage() {
        want.exception(() ->
                new PgStudentQuery().selectId().limit(1, 10).of(mapper).listEntity()
            , SQLSyntaxErrorException.class, BadSqlGrammarException.class);

        db.sqlList().wantFirstSql().eq("" +
            "SELECT \"id\" FROM test.\"student\" LIMIT ? OFFSET ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(10, 1);
    }

    @DisplayName("pg insert")
    @Test
    void testInsert() {
        want.exception(() ->
                mapper.insert(new PgStudentEntity().setAddress("address").setAge(29)),
            SQLSyntaxErrorException.class, BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO test.\"student\"(\"address\", \"age\") VALUES (?, ?)", StringMode.SameAsSpace);
    }
}