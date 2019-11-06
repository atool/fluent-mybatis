package cn.org.atool.fluent.mybatis.generator;

import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import org.junit.jupiter.api.Test;

public class MybatisGeneratorTest {
    private static String url = "jdbc:mysql://localhost:3306/mbplus";

    @Test
    public void generate() {
        String outdir = System.getProperty("user.dir") + "/../fluent-mybatis/src/test/java";
        new MybatisGenerator("cn.org.atool.fluent.mybatis.demo")
                .setOutputDir(outdir, outdir, outdir)
                .setEntitySetChain(true)
                .setDataSource(url, "root", "password")
                .addBaseDaoInterface("MyCustomerInterface<${entity}, ${query}, ${update}>", "cn.org.atool.fluent.mybatis.demo.MyCustomerInterface")
                .generate(new TableConvertor("t_") {
                            {
                                this.table("address");
                                this.table("t_user").setPartition(true);
                            }
                        }.allTable(table -> {
                            table.column("is_deleted", DbColumnType.BOOLEAN)
                                    .setGmtCreateColumn("gmt_created")
                                    .setGmtModifiedColumn("gmt_modified")
                                    .setVersionColumn("version")
                                    .setLogicDeletedColumn("is_deleted");
                        })
                );
    }
}