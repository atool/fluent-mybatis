package cn.org.atool.fluent.generator;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.test4j.module.database.proxy.DataSourceCreator;

import javax.sql.DataSource;

public class SqlLiteGenerator {
    DataSource sqliteDataSource() {
        return DataSourceCreator.create("dataSource",
            DbType.SQLITE, null,
            "jdbc:sqlite:" + System.getProperty("user.dir") + "/db/sqlite_test",
            "", ""
        );
    }

    @Disabled
    @Test
    void generate() {
        FileGenerator.build(sqliteDataSource(), A.class);
    }

    @Tables(
        dbType = DbType.SQLITE,
        driver = "org.sqlite.JDBC",
        url = "jdbc:sqlite:db/sqlite_test",
        username = "", password = "",
        basePack = "cn.org.atool.fluent.mybatis.db.sqlite", schema = "",
        tables = {
            @Table(value = {"student:SqliteStudent"})
        },
        /* 只是测试需要, 正式项目请生产文件到对应的src目录下 */
        srcDir = "src/main/java", testDir = "target/sqlite/test", daoDir = "target/sqlite/dao")
    static class A {
    }
}