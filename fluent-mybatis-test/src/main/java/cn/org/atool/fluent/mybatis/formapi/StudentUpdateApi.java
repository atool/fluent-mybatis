package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.form.annotation.BehaviorType;
import cn.org.atool.fluent.form.annotation.Behavior;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@FormService(entityClass = StudentEntity.class)
public interface StudentUpdateApi {
    @Behavior(entityClass = StudentEntity.class, type = BehaviorType.Save)
    Student saveStudent(Student student);

    int updateStudent(StudentUpdater student);

    @Data
    @Accessors(chain = true)
    class Student implements Serializable {
        private String userName;

        private Integer age;

        private Long id;
    }
}