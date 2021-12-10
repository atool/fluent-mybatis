package cn.org.atool.fluent.form.validator;

import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unused"})
class ValidKit2Test extends Test4J {

    @Test
    void validate() {
        want.exception(() -> Validation.validate(null, new Car()), IllegalArgumentException.class)
            .contains(arr("driver.age: 不能为null",
                "licensePlate: 不能为null",
                "manufacturer: 不能为空",
                "driver.hasDrivingLicense: 不能为null",
                "driver.name: 不能为空"));
    }

    @Test
    void validateList() {
        want.exception(() -> Validation.validate(null, list(new Car())), IllegalArgumentException.class)
            .contains(arr("[0].driver.age: 不能为null",
                "[0].licensePlate: 不能为null",
                "[0].manufacturer: 不能为空",
                "[0].driver.hasDrivingLicense: 不能为null",
                "[0].driver.name: 不能为空"));
    }

    static class Car {
        @NotBlank
        private String manufacturer;

        @NotNull
        @Size(min = 2, max = 14)
        private String licensePlate;

        @Min(2)
        @Max(5)
        private Integer seatCount;

        @AssertTrue
        private Boolean registered;

        @AssertTrue(message = "The car has to pass the vehicle inspection first")
        private Boolean passedVehicleInspection;

        @Valid
        @NotNull
        private final Driver driver = new Driver();

        @Valid
        @Size(max = 2)
        private final List<Person> passengers = new ArrayList<>();
        private String brand;

        @Range(min = 2, max = 4) // 作用同@Min @Max
        private Integer doors;
    }

    static class Driver extends Person {
        @NotNull
        @Min(value = 18, message = "必须年满18岁")
        public Integer age;
        @NotNull
        @AssertTrue(message = "必须具有驾照")
        public Boolean hasDrivingLicense;
    }

    static class Person {
        private final long personId = 0;
        @NotBlank
        @CheckCase(value = CaseMode.UPPER, message = "名字必须为大写")
        private String name;
        public Date birthday;
    }
}