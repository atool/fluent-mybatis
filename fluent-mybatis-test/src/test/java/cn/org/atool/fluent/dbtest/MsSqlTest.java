package cn.org.atool.fluent.dbtest;

import cn.org.atool.fluent.mybatis.db.mssql.mapper.MsUserMapper;
import cn.org.atool.fluent.mybatis.db.mssql.wrapper.MsUserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class MsSqlTest extends BaseTest {
    @Autowired
    MsUserMapper mapper;

    @DisplayName("验证sqlserver数据库分页")
    @Test
    void testPage() {
//        want.exception(() ->
        new MsUserQuery().selectId()
            .limit(1, 10)
            .orderBy.id().asc().end()
            .of(mapper).listEntity();
//            , SQLSyntaxErrorException.class, BadSqlGrammarException.class);

        db.sqlList().wantFirstSql().eq("" +
                "SELECT [id] FROM my_test.[my_user] " +
                "ORDER BY [id] ASC " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
            , StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(1, 10);
    }
}