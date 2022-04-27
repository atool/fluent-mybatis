package cn.org.atool.fluent.mybatis.test.formservice;

import cn.org.atool.fluent.mybatis.formservice.model.Student;
import cn.org.atool.fluent.mybatis.formservice.restapi.StudentRestApi;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.modes.EqMode;

public class FormApiTest extends BaseTest {
    @Autowired
    StudentRestApi api;

    @Test
    void createStudent() {
        ATM.dataMap.student.cleanTable();
        Student student = api.save(new Student().setUserName("test").setAge(34));
        want.object(student).eqReflect(new Student().setUserName("test").setAge(34), EqMode.IGNORE_DEFAULTS);
        want.number(student.getId()).isGt(0L);
    }
}
