package cn.org.atool.fluent.generator;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class MsSqlGenerator {
    @Disabled
    @Test
    void generate() {
        FileGenerator.build(A.class);
    }

    @Tables(
        dbType = DbType.SQL_SERVER2012,
        driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver",
        url = "jdbc:sqlserver://localhost:1433;database=TestDB",
        username = "sa", password = "Sql2017Server!",
        basePack = "cn.org.atool.fluent.mybatis.db.mssql", schema = "my_test",
        tables = {
            @Table(value = {"my_user:MsUser"})
        },
        /* 只是测试需要, 正式项目请生产文件到对应的src目录下 */
        srcDir = "src/main/java", testDir = "target/mssql/test", daoDir = "target/mssql/dao")
    static class A {
    }
}