package cn.org.atool.fluent.form.kits;

import cn.org.atool.fluent.form.meta.MethodArgNames;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.integration.DataProvider;
import org.test4j.junit5.Test4J;
import org.test4j.tools.Kits;

import static cn.org.atool.fluent.form.meta.MethodArgNames.build;

class MethodArgNamesKitTest extends Test4J {

    @ParameterizedTest
    @MethodSource("data4parseFindFields")
    void parseFindFields(String method, MethodArgNames expected) {
        MethodArgNames items = MethodArgNamesKit.parseMethodStyle(method);
        want.bool(items.isAnd).is(expected.isAnd);
        want.list(items.names).eqReflect(expected.names);
    }

    static DataProvider data4parseFindFields() {
        return new DataProvider()
            .data("findByNameAndAge", MethodArgNames.build(true, Kits.list("name", "age")))
            .data("findByNameOrAge", MethodArgNames.build(false, Kits.list("name", "age")))
            .data("findByFirstNameAndLastName", MethodArgNames.build(true, Kits.list("firstName", "lastName")))
            ;
    }
}