package cn.org.atool.fluent.generator;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.test4j.module.database.proxy.DataSourceCreator;

import javax.sql.DataSource;

import static cn.org.atool.fluent.generator.FluentMyBatisGeneratorMain.BasePack;

public class ClickHouseGenerator {
    DataSource derbyDataSource() {
        return DataSourceCreator.create("dataSource",
            DbType.CLICK_HOUSE, null,
            "jdbc:clickhouse://localhost:8123/test",
            "default", "", "test"
        );
    }

    @Disabled
    @Test
    void generate() {
        FileGenerator.build( A.class);
    }

    @Tables(
        dbType = DbType.CLICK_HOUSE,
        driver = "ru.yandex.clickhouse.ClickHouseDriver",
        url = "jdbc:clickhouse://localhost:8123/test",
        username = "default", password = "",
        basePack = BasePack, schema = "test",
        tables = {
            @Table(value = {"user"})
        },
        /* 只是测试需要, 正式项目请生产文件到对应的src目录下 */
        srcDir = "target/clickhouse/entity", testDir = "target/clickhouse/test", daoDir = "target/clickhouse/dao")
    static class A {
    }
}