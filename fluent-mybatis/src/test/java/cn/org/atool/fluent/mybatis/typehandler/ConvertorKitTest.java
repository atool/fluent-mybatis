package cn.org.atool.fluent.mybatis.typehandler;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.integration.DataProvider;
import org.test4j.junit5.Test4J;

import java.util.Date;

@SuppressWarnings("rawtypes")
class ConvertorKitTest extends Test4J {

    @ParameterizedTest
    @MethodSource("data4convertValueToClass")
    void convertValueToClass(Object input, Class klass, Object expected) {
        Object value = ConvertorKit.convertValueToClass(input, klass);
        want.object(value).eq(expected);
    }

    public static DataProvider data4convertValueToClass() {
        return new DataProvider()
            .data("E1", ETest.class, ETest.E1)
            .data(ETest.E2, String.class, "E2")
            .data(1, long.class, 1L)
            .data(1L, int.class, 1)
            .data(1, boolean.class, true)
            .data(0L, boolean.class, false)
            .data("TRUE", boolean.class, true)
            .data("XX", boolean.class, false)
            ;
    }

    @MethodSource("data4convertDate")
    @ParameterizedTest
    void convertDate(Object value, String expected) {
        Date date = ConvertorKit.convertValueToClass(value, Date.class);
        want.date(date).eqByFormat(expected);
    }

    public static DataProvider data4convertDate() {
        return new DataProvider()
            .data("2022-11-30 18:58:12", "2022-11-30 18:58:12")
            .data("2022-11-30 18:58:12.000", "2022-11-30 18:58:12")
            .data("2022-11-30", "2022-11-30 00:00:00")
            .data(1669805892000L, "2022-11-30 18:58:12");
    }

    public enum ETest {
        E1,
        E2,
        E3
    }
}