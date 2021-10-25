package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.form.annotation.ApiMethod;
import cn.org.atool.fluent.form.annotation.FormApi;
import cn.org.atool.fluent.form.annotation.MethodType;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@FormApi
public interface StudentCreatorApi {
    @ApiMethod(entity = StudentEntity.class, type = MethodType.Insert)
    Student saveStudent(Student student);

    @Data
    @Accessors(chain = true)
    class Student implements Serializable {
        private long id;

        private String userName;

        private int age;
    }
}