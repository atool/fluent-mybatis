package cn.org.atool.fluent.dbtest;

import cn.org.atool.fluent.mybatis.db.derby.mapper.DerbyStudentMapper;
import cn.org.atool.fluent.mybatis.db.derby.wrapper.DerbyStudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.sql.SQLSyntaxErrorException;

@SuppressWarnings("unchecked")
public class DerbyTest extends BaseTest {
    @Autowired
    DerbyStudentMapper mapper;

    @DisplayName("验证derby数据库分页")
    @Test
    void testPage() {
        want.exception(() ->
                new DerbyStudentQuery().selectId().limit(1, 10).of(mapper).listEntity()
            , SQLSyntaxErrorException.class, BadSqlGrammarException.class);

        db.sqlList().wantFirstSql().eq("" +
            "SELECT \"id\" FROM  TEST.\"student\" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(1, 10);
    }
}