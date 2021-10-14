package cn.org.atool.fluent.generator;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.test4j.module.database.proxy.DataSourceCreator;

import javax.sql.DataSource;

public class HsqldbGenerator {
    DataSource hsqlDataSource() {
        return DataSourceCreator.create("dataSource",
            DbType.HSQL, null,
            "jdbc:hsqldb:file:" + System.getProperty("user.dir") + "/db/hsql_test",
            "sa", "sa"
        );
    }

    @Disabled
    @Test
    void generate() {
        FileGenerator.build(hsqlDataSource(), A.class);
    }

    @Tables(
        dbType = DbType.HSQL,
        driver = "org.hsqldb.jdbcDriver",
        url = "jdbc:hsqldb:file:./db/hsql_test",
        username = "sa", password = "sa",
        basePack = "cn.org.atool.fluent.mybatis.db.hsql", schema = "PUBLIC.PUBLIC",
        tables = {
            @Table(value = {"STUDENT:HSqlStudent"})
        },
        /* 只是测试需要, 正式项目请生产文件到对应的src目录下 */
        srcDir = "src/main/java", testDir = "target/hsql/test", daoDir = "target/hsql/dao")
    static class A {
    }
}