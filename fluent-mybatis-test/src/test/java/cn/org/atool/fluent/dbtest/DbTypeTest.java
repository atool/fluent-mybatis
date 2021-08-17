package cn.org.atool.fluent.dbtest;

import cn.org.atool.fluent.mybatis.db.oracle11.mapper.OracleMapper;
import cn.org.atool.fluent.mybatis.db.oracle11.wrapper.OracleQuery;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class DbTypeTest extends BaseTest {
    @Autowired
    OracleMapper mapper;

    @DisplayName("验证数据库自定义特性: 反义符+分页")
    @Test
    void testPage() {
        DbType.ORACLE.setEscapeExpress("[?]");
        want.exception(() ->
            new OracleQuery().selectId().limit(1, 10).of(mapper).listEntity(), Exception.class);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT * FROM (" +
            "SELECT TMP_PAGE.*, ROWNUM RN FROM (SELECT [id] FROM [oracle_table]) TMP_PAGE) " +
            "WHERE RN > ? AND RN <= ?/**测试而已**/", StringMode.SameAsSpace);

        db.sqlList().wantFirstPara().eqList(1, 11);
    }
}
