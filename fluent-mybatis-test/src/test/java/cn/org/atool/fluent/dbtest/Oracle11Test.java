package cn.org.atool.fluent.dbtest;

import cn.org.atool.fluent.mybatis.base.intf.BatchCrud;
import cn.org.atool.fluent.mybatis.db.oracle11.entity.OracleUserEntity;
import cn.org.atool.fluent.mybatis.db.oracle11.mapper.OracleUserMapper;
import cn.org.atool.fluent.mybatis.db.oracle11.wrapper.OracleUserUpdate;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

@SuppressWarnings({})
@Disabled
class Oracle11Test extends BaseTest {
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

    @Test
    void test_Insert() {
        OracleUserEntity e1 = newEntity(null, "code1");
        userMapper.insert(e1);
        db.sqlList().wantFirstSql()
            .eq("SELECT TEST_USER_SEQ.nextval AS ID FROM DUAL");
        db.sqlList().wantSql(1).eq("" +
                "INSERT INTO TEST_USER (ID, IS_DELETED, CODE, VERSION2) " +
                "VALUES (?, ?, ?, ?)"
            , StringMode.SameAsSpace);
        want.number(e1.getId()).isGt(0L);
    }

    @Test
    void test_InsertWithPk() {
        OracleUserEntity e1 = newEntity(34L, "code1");
        userMapper.insertWithPk(e1);
        db.sqlList().wantSql(0).eq("" +
                "INSERT INTO TEST_USER (ID, IS_DELETED, CODE, VERSION2) " +
                "VALUES (?, ?, ?, ?)"
            , StringMode.SameAsSpace);
        want.number(e1.getId()).eq(34L);
    }

    @Test
    void test_batchInsert() {
        OracleUserEntity e1 = newEntity(null, "code1");
        OracleUserEntity e2 = newEntity(null, "code2");
        userMapper.insertBatch(list(e1, e2));
        db.sqlList().wantSql(0).eq("" +
                "INSERT INTO TEST_USER (ID, IS_DELETED, CODE, VERSION2) " +
                "SELECT TEST_USER_SEQ.nextval AS ID, TMP.* FROM (" +
                " (SELECT ? , ? , ? FROM DUAL)" +
                " UNION ALL" +
                " (SELECT ? , ? , ? FROM DUAL) " +
                ") TMP"
            , StringMode.SameAsSpace);
    }

    @Test
    void test_batchInsertWithPk() {
        OracleUserEntity e1 = newEntity(21L, "code1");
        OracleUserEntity e2 = newEntity(22L, "code2");

        userMapper.insertBatchWithPk(list(e1, e2));
        db.sqlList().wantFirstSql().eq("" +
                "INSERT INTO TEST_USER (ID, IS_DELETED, CODE, VERSION2) " +
                "SELECT TMP.* " +
                "FROM ( (SELECT ? , ? , ? , ? FROM DUAL) UNION ALL (SELECT ? , ? , ? , ? FROM DUAL) ) TMP"
            , StringMode.SameAsSpace);
    }

    @Test
    void testBatchCRUD() {
        userMapper.batchCrud(BatchCrud.batch()
            .addInsert(newEntity(11L, "code"))
            .addUpdate(new OracleUserUpdate()
                .set.code().is("code2").end()
                .where.id().eq(11L).end())
        );
        db.sqlList().wantFirstSql().eq("" +
            "BEGIN " +
            "INSERT INTO TEST_USER (ID, IS_DELETED, CODE, VERSION2) " +
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