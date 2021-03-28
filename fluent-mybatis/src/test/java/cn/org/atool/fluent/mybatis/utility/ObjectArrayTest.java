package cn.org.atool.fluent.mybatis.utility;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.integration.DataProvider;
import org.test4j.junit5.Test4J;

import java.util.Iterator;

class ObjectArrayTest extends Test4J {

    @MethodSource("dataToBoolean")
    @ParameterizedTest
    void toBoolean(Object value, Boolean expected) {
        Boolean actual = ObjectArray.toBoolean(value);
        want.bool(actual == expected).is(true);
    }

    public static Iterator dataToBoolean() {
        return new DataProvider()
            .data(null, null)
            .data("1", true)
            .data("0", false)
            .data(1, true)
            .data(0, false)
            .data("-1", true)
            .data(-1, true)
            .data(true, true)
            .data(false, false)
            .data("trUe", true)
            .data("xx", false)
            ;
    }
}