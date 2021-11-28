package cn.org.atool.fluent.form.kits;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.integration.DataProvider;
import org.test4j.junit5.Test4J;

class FinderNameKitTest extends Test4J {

    @ParameterizedTest
    @MethodSource("data4parseFindFields")
    void parseFindFields(String method, String[] expected) {
        String[] items = FinderNameKit.parseFindFields(method);
        want.array(items).eq(expected);
    }

    static DataProvider data4parseFindFields() {
        return new DataProvider()
            .data("findByNameAndAge", new String[]{"name", "age"})
            .data("findByFirstNameAndLastName", new String[]{"firstName", "lastName"})
            .data("findNameAndAge", null)
            ;
    }
}