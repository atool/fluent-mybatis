package cn.org.atool.fluent.dbtest;

import cn.org.atool.fluent.mybatis.base.BatchCrud;
import cn.org.atool.fluent.mybatis.db.oracle11.entity.OracleUserEntity;
import cn.org.atool.fluent.mybatis.db.oracle11.mapper.OracleMapper;
import cn.org.atool.fluent.mybatis.db.oracle11.mapper.OracleUserMapper;
import cn.org.atool.fluent.mybatis.db.oracle11.wrapper.OracleQuery;
import cn.org.atool.fluent.mybatis.db.oracle11.wrapper.OracleUserUpdate;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.tools.Kits;

@SuppressWarnings({"unchecked"})
class Oracle11Test extends BaseTest {
    @Autowired
    OracleMapper mapper;

    @Autowired
    OracleUserMapper userMapper;

    private OracleUserEntity newEntity(Long id, String code) {
        return new OracleUserEntity().setId(id).setCode(code).setIsDeleted(false).setVersion2(0L);
    }

    @BeforeEach
    void setup() {
        DbType.ORACLE.setEscapeExpress("?");
        db.table("TEST_USER").clean();
    }

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
    @Disabled
    void test_Insert() {
        OracleUserEntity e1 = newEntity(null, "code1");
        userMapper.insert(e1);
        db.sqlList().wantFirstSql()
            .eq("SELECT TEST_USER_SEQ.nextval AS ID FROM DUAL");
        db.sqlList().wantSql(1).eq("" +
                "INSERT INTO TEST_USER(ID, CODE, IS_DELETED, VERSION2) " +
                "VALUES (?, ?, ?, ?)"
            , StringMode.SameAsSpace);
        want.number(e1.getId()).isGt(0L);
    }

    @Test
    void test_InsertWithPk() {
        OracleUserEntity e1 = newEntity(34L, "code1");
        userMapper.insertWithPk(e1);
        db.sqlList().wantSql(0).eq("" +
                "INSERT INTO TEST_USER(ID, CODE, IS_DELETED, VERSION2) " +
                "VALUES (?, ?, ?, ?)"
            , StringMode.SameAsSpace);
        want.number(e1.getId()).eq(34L);
    }

    @Test
    void test_batchInsert() {
        OracleUserEntity e1 = newEntity(null, "code1");
        OracleUserEntity e2 = newEntity(null, "code2");
//        want.exception(() ->
        userMapper.insertBatch(Kits.arr(e1, e2));
//            , Exception.class);
        System.out.println(e1.getId());
//        db.sqlList().wantSql(0).eq("" +
//            "SELECT TEST_USER_SEQ.nextval AS ID FROM DUAL");
        db.sqlList().wantSql(0).eq("" +
                "INSERT INTO TEST_USER(ID, CODE, IS_DELETED, VERSION2) SELECT TEST_USER_SEQ.nextval AS ID, TMP.* FROM ( SELECT ?, ?, ? FROM dual UNION ALL SELECT ?, ?, ? FROM dual ) TMP"
            , StringMode.SameAsSpace);
    }

    @Test
    void test_batchInsertWithPk() {
        OracleUserEntity e1 = newEntity(21L, "code1");
        OracleUserEntity e2 = newEntity(22L, "code2");

//        want.exception(() ->
        userMapper.insertBatchWithPk(Kits.arr(e1, e2));
//            , Exception.class);
        db.sqlList().wantFirstSql().eq("" +
                "INSERT INTO TEST_USER(ID, CODE, IS_DELETED, VERSION2) " +
                "SELECT TMP.* " +
                "FROM ( SELECT ?, ?, ?, ? FROM dual UNION ALL SELECT ?, ?, ?, ? FROM dual ) TMP"
            , StringMode.SameAsSpace);
    }

    @Test
    void testBatchCRUD() {
        userMapper.batchCrud(BatchCrud.batch()
            .setDbType(DbType.ORACLE)
            .addInsert(newEntity(11L, "code"))
            .addUpdate(new OracleUserUpdate()
                .set.code().is("code2").end()
                .where.id().eq(11L).end())
        );
        db.sqlList().wantFirstSql().eq("" +
            "BEGIN " +
            "INSERT INTO TEST_USER(ID, CODE, IS_DELETED, VERSION2) " +
            "VALUES (?, ?, ?, ?); " +
            "UPDATE TEST_USER SET CODE = ? WHERE ID = ?; " +
            "END;");
    }

    @Test
    void testBatchUpdate() {
        userMapper.updateBy(new OracleUserUpdate()
                .set.code().is("code2").end()
                .where.id().eq(11L).end(),
            new OracleUserUpdate()
                .set.code().is("code2").end()
                .where.id().eq(12L).end()
        );
        db.sqlList().wantFirstSql().eq("" +
            "BEGIN " +
            "UPDATE TEST_USER SET CODE = ? WHERE ID = ?; " +
            "UPDATE TEST_USER SET CODE = ? WHERE ID = ?; " +
            "END;");
    }
}