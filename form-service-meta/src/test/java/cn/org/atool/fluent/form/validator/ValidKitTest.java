package cn.org.atool.fluent.form.validator;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unused"})
class ValidKitTest extends Test4J {

    @Test
    void validate() {
        want.exception(() -> Validation.validate(new Car()), IllegalArgumentException.class)
            .contains(arr("driver.age: 不能为null",
                "licensePlate: 不能为null",
                "manufacturer: 不能为空",
                "driver.hasDrivingLicense: 不能为null",
                "driver.name: 不能为空"));
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