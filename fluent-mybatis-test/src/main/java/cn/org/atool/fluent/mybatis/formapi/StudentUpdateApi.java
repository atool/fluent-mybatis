package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.form.annotation.ServiceMethod;
import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@FormService
public interface StudentUpdateApi {
    @ServiceMethod(entityClass = StudentEntity.class)
    int updateStudent(Student student);

    @Data
    @Accessors(chain = true)
    class Student implements Serializable {
        @Entry(type = EntryType.Update)
        private String userName;

        @Entry(type = EntryType.Update)
        private Integer age;

        private Long id;
    }
}