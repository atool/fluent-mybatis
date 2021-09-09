package cn.org.atool.fluent.generator;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Oracle11gGenerator {
    /**
     * create table TEST_USER
     * (
     * ID NUMBER(19) not null
     * constraint TEST_USER_PK
     * primary key,
     * IS_DELETED NUMBER(19) default 0,
     * CODE VARCHAR2(100),
     * VERSION2 NUMBER(19) default 0
     * );
     * <p>
     * CREATE SEQUENCE TEST_USER_SEQ;
     */

    @Disabled
    @Test
    void generate() {
        FileGenerator.build(A.class);
    }

    @Tables(
        dbType = DbType.ORACLE,
        driver = "oracle.jdbc.OracleDriver",
        url = "jdbc:oracle:thin:@localhost:1521:helowin",
        username = "system", password = "system",
        basePack = "cn.org.atool.fluent.mybatis.db.oracle11", schema = "SYSTEM",
        tables = {
            @Table(value = {"TEST_USER:OracleUser"},
                seqName = "SELECT TEST_USER_SEQ.nextval AS ID FROM DUAL")
        }, logicDeleted = "IS_DELETED",
        /* 只是测试需要, 正式项目请生产文件到对应的src目录下 */
        srcDir = "src/main/java", testDir = "target/oracle/test", daoDir = "target/oracle/dao")
    static class A {
    }
}