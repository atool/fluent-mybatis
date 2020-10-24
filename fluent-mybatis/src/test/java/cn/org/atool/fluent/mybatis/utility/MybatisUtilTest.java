package cn.org.atool.fluent.mybatis.utility;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

class MybatisUtilTest extends Test4J {

    @ParameterizedTest
    @MethodSource("dataForUnderlineToCamel")
    void underlineToCamel(String underline, String camel) {
        String result = MybatisUtil.underlineToCamel(underline, false);
        want.string(result).eq(camel);
    }

    public static DataProvider dataForUnderlineToCamel() {
        return new DataProvider()
            .data(null, "")
            .data("__A_bc_", "aBc")
            .data("A_b_", "aB")
            .data("a_b_", "aB")
            .data("__a_bc_", "aBc")
            .data("__a_b_", "aB")
            .data("_a", "a")
            ;
    }
}