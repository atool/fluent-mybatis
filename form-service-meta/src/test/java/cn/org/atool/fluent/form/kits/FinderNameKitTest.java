package cn.org.atool.fluent.form.kits;

import cn.org.atool.fluent.form.meta.NameAndPair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.integration.DataProvider;
import org.test4j.junit5.Test4J;
import org.test4j.tools.Kits;

import java.util.List;

import static cn.org.atool.fluent.form.meta.NameAndPair.pair;

class FinderNameKitTest extends Test4J {

    @ParameterizedTest
    @MethodSource("data4parseFindFields")
    void parseFindFields(String method, List<NameAndPair> expected) {
        List<NameAndPair> items = FinderNameKit.parseFindFields(method);
        want.list(items).eqReflect(expected);
    }

    static DataProvider data4parseFindFields() {
        return new DataProvider()
            .data("findByNameAndAge", Kits.list(pair("name", true), pair("age", true)))
            .data("findByNameOrAge", Kits.list(pair("name", true), pair("age", false)))
            .data("findByFirstNameAndLastName", Kits.list(pair("firstName", true), pair("lastName", true)))
            .data("findNameAndAge", null)
            ;
    }
}