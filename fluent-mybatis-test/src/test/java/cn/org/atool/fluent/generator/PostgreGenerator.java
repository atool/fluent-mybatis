package cn.org.atool.fluent.generator;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.test4j.module.database.proxy.DataSourceCreator;

import javax.sql.DataSource;

public class PostgreGenerator {
    DataSource postgreDataSource() {
        return DataSourceCreator.create("dataSource",
            DbType.POSTGRE_SQL, null,
            "jdbc:postgresql://localhost:5432/postgres",
            "postgres", "", "test"
        );
    }

    @Disabled
    @Test
    void generate() {
        FileGenerator.build(postgreDataSource(), A.class);
    }

    @Tables(
        dbType = DbType.POSTGRE_SQL,
        driver = "org.postgresql.Driver",
        url = "jdbc:postgresql://localhost:5432/postgres",
        username = "postgres", password = "",
        basePack = "cn.org.atool.fluent.mybatis.db.pg", schema = "test",
        tables = {
            @Table(value = {"student:PgStudent"})
        },
        /* 只是测试需要, 正式项目请生产文件到对应的src目录下 */
        srcDir = "src/main/java", testDir = "target/postgresql/test", daoDir = "target/postgresql/dao")
    static class A {
    }
}