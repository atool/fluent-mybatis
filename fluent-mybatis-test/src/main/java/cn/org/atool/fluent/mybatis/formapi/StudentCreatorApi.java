package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.form.annotation.*;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@FormService
public interface StudentCreatorApi {
    @ServiceMethod(entityClass = StudentEntity.class, type = MethodType.Save)
    Student saveStudent(Student student);

    @Data
    @Accessors(chain = true)
    class Student implements Serializable {
        private String userName;

        private Integer age;

        private Long id;
    }
}