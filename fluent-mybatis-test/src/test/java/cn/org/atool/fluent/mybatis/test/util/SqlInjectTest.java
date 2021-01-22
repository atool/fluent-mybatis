package cn.org.atool.fluent.mybatis.test.util;

import cn.org.atool.fluent.mybatis.utility.SqlInject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.integration.DataProvider;
import org.test4j.junit5.Test4J;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author darui.wu
 * @create 2019/10/31 7:10 下午
 */
public class SqlInjectTest extends Test4J {
    @MethodSource("data_isKeyword")
    @ParameterizedTest
    public void test_isKeyword(String string, boolean isKeyword) {
        boolean result = SqlInject.probablySqlInject(string);
        want.bool(result).is(isKeyword);
    }

    public static DataProvider data_isKeyword() {
        return new DataProvider()
            .data("and", true)
            .data("vand", false)
            .data("ande", false)
            .data(" and", true)
            .data("\tand ", true)
            .data("ANd", true)
            .data("adfasdf and afdasf", true)
            .data("adfad grAnt dafd", true)
            .data("adfad GRANT\ndafd", true)
            .data("adfad GRANT", true)
            .data("GRANt dddd", true)
            .data("adfad GRAN", false)
            .data("adfad grAnt_", false)
            ;
    }


    @MethodSource("data_isDanger")
    @ParameterizedTest
    public void test_isDanger(String string, boolean isKeyword) {
        boolean result = SqlInject.probablySqlInject(string);
        want.bool(result).is(isKeyword);
    }

    public static DataProvider data_isDanger() {
        return new DataProvider()
            .data("--", true)
            .data("-", false)
            .data("aaa--bbb", true)
            .data("aaa-d-bbb", false)
            .data("aaa#bbb", true)
            .data("aaa%bbb", true)
            .data("aaa/bbb", true)
            .data("aaa'bbb", true)
            .data("aaa\\bbb", true)
            .data("aaa*bbb", true)
            ;
    }


    @MethodSource("data_simpleNoInject")
    @ParameterizedTest
    public void test_simpleNoInject(String string, boolean isKeyword) {
        boolean result = SqlInject.hasSimpleInject(string);
        want.bool(result).is(isKeyword);
    }

    public static DataProvider data_simpleNoInject() {
        return new DataProvider()
            .data("--", true)
            .data("---aaa", true)
            .data("and", false)
            .data("-", false)
            .data("aaa--bbb", true)
            .data("aaa-d-bbb", false)
            .data("aaa#bbb", true)
            .data("aaa%bbb", true)
            .data("aaa/bbb", true)
            .data("aaa'bbb", true)
            .data("aaa\\bbb", true)
            .data("aaa*bbb", true)
            ;
    }

    @Test
    public void test_assertSimpleNoInject() {
        assertThrows(RuntimeException.class, () ->
            SqlInject.assertSimpleNoInject("test", "--")
        );
    }

    @Test
    public void test_assertNoInject() {
        assertThrows(RuntimeException.class, () ->
            SqlInject.assertNoInject("test", "and")
        );
    }
}
