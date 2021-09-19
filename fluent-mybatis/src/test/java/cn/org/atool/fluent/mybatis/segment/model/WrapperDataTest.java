package cn.org.atool.fluent.mybatis.segment.model;

import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.hamcrest.IWant;
import org.test4j.integration.DataProvider;

import java.util.Iterator;
import java.util.List;

import static org.test4j.tools.commons.ListHelper.toList;

@SuppressWarnings("all")
class WrapperDataTest implements IWant {

    @MethodSource("data4parseAlias")
    @ParameterizedTest
    void parseAlias(String select, List<String> expected) {
        List<String> list = MybatisUtil.parseAlias(select);
        want.list(list).eq(expected);
    }

    static Iterator data4parseAlias() {
        return new DataProvider()
            .data("xxx", toList())
            .data("axx as ccc", toList("ccc"))
            .data("as As ss,dd aS dd , bb as  d3", toList("ss", "dd", "d3"))
            .data("thisAsddd", toList());
    }
}