package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.form.annotation.*;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@FormService(entityClass = StudentEntity.class)
public interface StudentUpdateApi {
    @ServiceMethod(entityClass = StudentEntity.class, type = MethodType.Save)
    Student saveStudent(Student student);

    int updateStudent(StudentUpdater student);

    @Data
    @Accessors(chain = true)
    class Student implements Serializable {
        private String userName;

        private Integer age;

        private Long id;
    }

    @Data
    @Accessors(chain = true)
    class StudentUpdater implements Serializable {
        @Entry(type = EntryType.Update)
        private String userName;

        @Entry(type = EntryType.Update)
        private Integer age;

        private Long id;
    }
}