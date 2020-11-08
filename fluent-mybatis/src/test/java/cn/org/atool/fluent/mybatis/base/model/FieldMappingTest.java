package cn.org.atool.fluent.mybatis.base.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

import static org.junit.jupiter.api.Assertions.*;

class FieldMappingTest extends Test4J {

    @ParameterizedTest
    @MethodSource("dataForIsColumnName")
    void isColumnName(String input, boolean expected) {
        boolean result = FieldMapping.isColumnName(input);
        want.bool(result).is(expected);
    }

    public static DataProvider dataForIsColumnName() {
        return new DataProvider()
            .data(null, false)
            .data(" ", false)
            .data("12131", false)
            .data("'xss'", false)
            .data("sum(id)", false)
            .data("sum_0", true)
            .data("sum_ss", true)
            .data("sum", true)
            ;
    }
}