package cn.org.atool.fluent.generator;

import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class H2Generator {
    static final String URL = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    static final String BasePack = "cn.org.atool.fluent.mybatis.generate";

    @Test
    @Disabled
    void generate() {
        FileGenerator.build(A.class);
    }

    @Tables(url = URL, username = "", password = "",
        basePack = BasePack,
        srcDir = "target/entity", testDir = "target/temp", daoDir = "target/temp",
        tables = {
            @Table(value = {"table1"})
        })
    class A {
    }
}
