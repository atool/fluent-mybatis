package cn.org.atool.fluent.mybatis.utility;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.integration.DataProvider;
import org.test4j.junit5.Test4J;

import java.util.Iterator;
import java.util.List;

import static org.test4j.tools.commons.ListHelper.toList;

@SuppressWarnings("all")
class CustomizedSqlTest extends Test4J {

    @MethodSource("data4parse")
    @ParameterizedTest
    void parse(String sql, List<String> expected) {
        List<String> list = CustomizedSql.parse(sql);
        want.list(list).eq(expected);
    }

    static Iterator data4parse() {
        return new DataProvider()
            .data("xd", toList("xd"))
            .data("xdd#{value}afs", toList("xdd", "#{value}", "afs"))
            .data("afs #{ ddd},,,#{ sfsdf}", toList("afs ", "#{ ddd}", ",,,", "#{ sfsdf}"))
            ;
    }
}