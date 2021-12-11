package cn.org.atool.fluent.form.validator;

import cn.org.atool.fluent.common.kits.ParameterizedTypes;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ParameterizedTypesTest extends Test4J {

    @Test
    void test() {
        Type pType = ParameterizedTypes.getType(MyTest.class, List.class, "E");
        want.object(pType).eq(String.class);
    }
}

class MyTest extends ArrayList<String> {
}