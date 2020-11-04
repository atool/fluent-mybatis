package cn.org.atool.fluent.mybatis.entity.generator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

class WrappersFileTest extends Test4J {

    @ParameterizedTest
    @MethodSource("dataForSameStartPackage")
    void sameStartPackage(String base1, String base2, String expected) {
        String base = WrappersFile.sameStartPackage(base1, base2);
        want.string(base).eq(expected);
    }

    public static DataProvider dataForSameStartPackage() {
        return new DataProvider()
            .data("", "z.b", "")
            .data(null, "z.b", "z.b")
            .data("a.b", null, "a.b")
            .data(null, null, null)
            .data("a.b.c", "a.b.z", "a.b");
    }
}