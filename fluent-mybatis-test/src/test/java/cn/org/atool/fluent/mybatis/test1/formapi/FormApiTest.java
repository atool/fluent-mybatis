package cn.org.atool.fluent.mybatis.test1.formapi;

import cn.org.atool.fluent.mybatis.formapi.StudentCreatorApi;
import cn.org.atool.fluent.mybatis.test1.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.modes.EqMode;

public class FormApiTest extends BaseTest {
    @Autowired
    StudentCreatorApi api;

    @Test
    void createStudent() {
        StudentCreatorApi.Student student = api.saveStudent(new StudentCreatorApi.Student().setUserName("test").setAge(34));
        want.object(student).eqReflect(new StudentCreatorApi.Student().setUserName("test").setAge(34), EqMode.IGNORE_DEFAULTS);
        want.number(student.getId()).isGt(0L);
    }
}