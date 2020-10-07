package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.generator.EntityGenerator;
import cn.org.atool.fluent.mybatis.generator.EntityGenerator2;
import cn.org.atool.fluent.mybatis.generator.annoatation.Table;
import cn.org.atool.fluent.mybatis.generator.annoatation.Tables;

import static cn.org.atool.fluent.mybatis.FluentMyBatisGeneratorMain.URL;

@Tables(url = URL, username = "root", password = "password",
    srcDir = "fluent-mybatis-test/src/main/java",
    entityPack = "cn.org.atool.fluent.mybatis.generate.entity",
    daoPack = "cn.org.atool.fluent.mybatis.generate.dao",

    daoInterface = MyCustomerInterface.class,
    tables = {
        @Table("address")
    })
public class FluentMyBatisGeneratorMain2 {
    public static final String URL = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    /**
     * 使用main函数，是避免全量跑test时，误执行生成代码
     *
     * @param args
     */
    public static void main(String[] args) {
        EntityGenerator.generate(FluentMyBatisGeneratorMain2.class);
    }
}