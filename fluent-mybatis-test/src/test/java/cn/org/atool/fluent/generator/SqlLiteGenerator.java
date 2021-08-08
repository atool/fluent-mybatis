package cn.org.atool.fluent.generator;

import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import cn.org.atool.generator.database.DbTypeOfGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.test4j.module.database.proxy.DataSourceCreator;
import org.test4j.module.database.script.DataSourceType;

import javax.sql.DataSource;

import static cn.org.atool.fluent.generator.FluentMyBatisGeneratorMain.BasePack;

public class SqlLiteGenerator {
    DataSource sqliteDataSource() {
        return DataSourceCreator.create("dataSource", DataSourceType.Sqlite,
            "org.sqlite.JDBC",
            "jdbc:sqlite:" + System.getProperty("user.dir") + "/db/sqlite_test",
            "", ""
        );
    }

    @Disabled
    @Test
    void generate() {
        FileGenerator.build( A.class);
    }

    @Tables(
        dbType = DbTypeOfGenerator.SQLITE,
        driver = "org.sqlite.JDBC",
        url = "jdbc:sqlite:db/sqlite_test",
        username = "", password = "",
        basePack = BasePack, schema = "",
        tables = {
            @Table(value = {"student"})
        },
        /* 只是测试需要, 正式项目请生产文件到对应的src目录下 */
        srcDir = "target/sqlite/entity", testDir = "target/sqlite/test", daoDir = "target/sqlite/dao")
    static class A {
    }
}
