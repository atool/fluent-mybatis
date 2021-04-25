package cn.org.atool.fluent.mybatis.utility;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.integration.DataProvider;
import org.test4j.junit5.Test4J;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isColumnName;

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

    @ParameterizedTest
    @MethodSource("dataOfIsColumn")
    void test_isColumn(String column, Boolean expected) {
        boolean result = isColumnName(column);
        want.bool(result).is(expected);
    }

    public static DataProvider dataOfIsColumn() {
        return new DataProvider()
            .data("ax_b", true)
            .data("_abc_", true)
            .data("  ", false)
            .data(null, false)
            .data("124", false)
            .data("-124", false)
            .data("a123", true)
            .data("a_12", true)
            .data("a.b", false)
            .data("'abc'", false)
            .data("111", false)
            .data("t1.x", false)
            .data("sum(1)", false)
            .data("sum(`abc`)", false)
            .data("abc ed", false)
            .data("_abc1", true)
            .data("_ABC1", true)
            .data("1we", false)
            .data("`xx`", true)
            .data("`xx", false)
            .data("xx`", false)
            ;
    }
}