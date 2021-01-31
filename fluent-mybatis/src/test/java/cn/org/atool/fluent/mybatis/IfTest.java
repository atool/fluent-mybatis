package cn.org.atool.fluent.mybatis;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.integration.DataProvider;
import org.test4j.junit5.Test4J;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class IfTest extends Test4J {

    @ParameterizedTest
    @MethodSource("dataOfIsEmpty")
    void isEmpty(Object value, boolean expected) {
        boolean result = If.isEmpty(value);
        want.bool(result).is(expected);
    }

    static DataProvider dataOfIsEmpty() {
        return new DataProvider()
            .data(null, true)
            .data(Arrays.asList(1, 2), false)
            .data(new ArrayList(), true)
            .data(new HashMap(), true)
            .data(new HashMap() {{
                this.put("1", "1");
            }}, false)
            .data(new int[0], true)
            .data(new int[]{1, 2}, false)
            .data(new String[0], true)
            .data(new String[]{""}, false)
            .data(new double[]{1, 2}, false)
            .data(new short[]{}, true)
            ;
    }
}