package cn.org.atool.fluent.generator;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.test4j.module.database.proxy.DataSourceCreator;

import javax.sql.DataSource;

public class H2Generator {
    DataSource h2DataSource() {
        return DataSourceCreator.create("dataSource",
            DbType.H2, null,
            "jdbc:h2:" + System.getProperty("user.dir") + "/db/h2test",
            "sa", "sa"
        );
    }

    @Disabled
    @Test
    void generate() {
        FileGenerator.build(h2DataSource(), A.class);
    }

    @Tables(
        dbType = DbType.H2,
        driver = "org.h2.Driver",
        url = "jdbc:h2:./db/h2test",
        username = "sa", password = "sa",
        basePack = "cn.org.atool.fluent.mybatis.db.h2", schema = "H2TEST.PUBLIC",
        tables = {
            @Table(value = {"STUDENT:H2Student"})
        },
        /* 只是测试需要, 正式项目请生产文件到对应的src目录下 */
        srcDir = "src/main/java", testDir = "target/h2/test", daoDir = "target/h2/dao")
    class A {
    }
}