package cn.org.atool.fluent.form.validator;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.lang.reflect.Method;

class ValidationTest extends Test4J {
    @Test
    void test() {
        Integer[] arr = new Integer[]{1};
        boolean result = arr instanceof Object[];
        want.bool(result).is(true);
    }

    @Test
    void validateMethod() throws Exception {
        Method method = ValidationTest.class.getDeclaredMethod("example", String.class, int.class);
        want.exception(() -> Validation.validate(new ValidationTest(), method, new Object[]{null, 3}),
                IllegalArgumentException.class)
            .contains("example.arg1: 最小不能小于4")
            .contains("example.arg0: 不能为null");
    }

    public void example(@NotNull String name, @Min(4) int age) {
    }
}