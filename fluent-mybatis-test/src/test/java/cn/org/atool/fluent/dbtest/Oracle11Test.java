package cn.org.atool.fluent.dbtest;

import cn.org.atool.fluent.mybatis.db.oracle11.entity.OracleEntity;
import cn.org.atool.fluent.mybatis.db.oracle11.mapper.OracleMapper;
import cn.org.atool.fluent.mybatis.db.oracle11.wrapper.OracleQuery;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.tools.Kits;

@SuppressWarnings({"unchecked"})
class Oracle11Test extends BaseTest {
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

    @Test
    void test_batchInsert() {
        DbType.ORACLE.setEscapeExpress("?");
        want.exception(() ->
                mapper.insertBatch(Kits.arr(
                    new OracleEntity().setCode("code1"),
                    new OracleEntity().setCode("code2")
                ))
            , Exception.class);
        db.sqlList().wantFirstSql().eq("" +
                "INSERT INTO oracle_table(id, is_deleted, code, version) " +
                "SELECT SEQ_USER_ID.nextval as id, TMP.* FROM ( " +
                "SELECT 0, ?, 0 FROM dual " +
                "UNION ALL " +
                "SELECT 0, ?, 0 FROM dual ) TMP"
            , StringMode.SameAsSpace);
    }


    @Test
    void test_batchInsertWithPk() {
        DbType.ORACLE.setEscapeExpress("?");
        want.exception(() ->
                mapper.insertBatch(Kits.arr(
                    new OracleEntity().setId(1L).setCode("code1"),
                    new OracleEntity().setId(2L).setCode("code2")
                ))
            , Exception.class);
        db.sqlList().wantFirstSql().eq("" +
                "INSERT INTO oracle_table(id, is_deleted, code, version) " +
                "SELECT SEQ_USER_ID.nextval as id, TMP.* FROM ( " +
                "SELECT 0, ?, 0 FROM dual " +
                "UNION ALL " +
                "SELECT 0, ?, 0 FROM dual ) TMP"
            , StringMode.SameAsSpace);
    }
}