package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.form.FormApi;

import java.io.Serializable;

@FormApi
public interface StudentCreatorApi {
    Student saveStudent(Student student);

    class Student implements Serializable {

    }
}
