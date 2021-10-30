package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.form.annotation.FormMethod;
import cn.org.atool.fluent.form.annotation.MethodType;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@FormService(entity = StudentEntity.class)
public interface StudentUpdateApi {
    @FormMethod(entity = StudentEntity.class, type = MethodType.Save)
    Student saveStudent(Student student);

    @FormMethod(type = MethodType.Update)
    int updateStudent(StudentUpdater student);

    @Data
    @Accessors(chain = true)
    class Student implements Serializable {
        private String userName;

        private Integer age;

        private Long id;
    }
}