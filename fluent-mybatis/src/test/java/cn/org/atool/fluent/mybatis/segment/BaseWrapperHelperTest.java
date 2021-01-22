package cn.org.atool.fluent.mybatis.segment;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.Test4J;
import org.test4j.integration.DataProvider;

class BaseWrapperHelperTest implements Test4J {
    @ParameterizedTest
    @MethodSource("dataOfIsColumn")
    void test_isColumn(String column, Boolean expected) {
        boolean result = BaseWrapperHelper.isColumnName(column);
        want.bool(result).is(expected);
    }

    public static DataProvider dataOfIsColumn() {
        return new DataProvider()
            .data("111", false)
            .data("t1.x", false)
            .data("sum(1)", false)
            .data("sum(`abc`)", false)
            .data("abc ed", false)
            .data("_abc1", true)
            .data("_ABC1", true)
            .data("1we", true)
            .data("`xx`", true)
            ;
    }
}