package cn.org.atool.fluent.generator;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.test4j.module.database.proxy.DataSourceCreator;

import javax.sql.DataSource;

public class DerbyGenerator {
    DataSource derbyDataSource() {
        return DataSourceCreator.create("dataSource",
            DbType.DERBY, null,
            "jdbc:derby:" + System.getProperty("user.dir") + "/db/derby_test;create=true",
            "sa", "sa", "TEST"
        );
    }

    @Disabled
    @Test
    void generate() {
        FileGenerator.build(A.class);
    }

    @Tables(
        dbType = DbType.DERBY,
        driver = "org.apache.derby.jdbc.EmbeddedDriver",
        url = "jdbc:derby:./db/derby_test;create=true",
        username = "sa", password = "sa",
        basePack = "cn.org.atool.fluent.mybatis.db.derby", schema = "TEST",
        tables = {
            @Table(value = {"student:DerbyStudent"})
        },
        /* 只是测试需要, 正式项目请生产文件到对应的src目录下 */
        srcDir = "src/main/java", testDir = "target/derby/test", daoDir = "target/derby/dao")
    static class A {
    }
}