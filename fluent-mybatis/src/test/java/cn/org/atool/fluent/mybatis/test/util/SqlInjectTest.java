package cn.org.atool.fluent.mybatis.test.util;

import cn.org.atool.fluent.mybatis.util.SqlInject;
import org.junit.Test;
import org.test4j.junit.DataFrom;
import org.test4j.junit.Test4J;
import org.test4j.tools.datagen.DataProvider;

/**
 * @author darui.wu
 * @create 2019/10/31 7:10 下午
 */
public class SqlInjectTest extends Test4J {
    @DataFrom("data_isKeyword")
    @Test
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


    @DataFrom("data_isDanger")
    @Test
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


    @DataFrom("data_simpleNoInject")
    @Test
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

    @Test(expected = RuntimeException.class)
    public void test_assertSimpleNoInject() {
        SqlInject.assertSimpleNoInject("test", "--");
    }

    @Test(expected = RuntimeException.class)
    public void test_assertNoInject() {
        SqlInject.assertNoInject("test", "and");
    }
}