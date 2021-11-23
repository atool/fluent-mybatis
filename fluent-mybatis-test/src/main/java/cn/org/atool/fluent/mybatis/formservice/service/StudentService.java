package cn.org.atool.fluent.mybatis.formservice.service;

import cn.org.atool.fluent.form.annotation.*;
import cn.org.atool.fluent.mybatis.formservice.model.*;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;

import java.util.List;

@SuppressWarnings("all")
@FormService(entity = StudentEntity.class)
public interface StudentService {
    @FormMethod(type = MethodType.Save)
    Student saveStudent(Student student);

    @FormMethod(type = MethodType.Save)
    boolean saveStudent(List<Student> students);

    @FormMethod(type = MethodType.Update)
    int updateStudent(StudentUpdater student);

    @FormMethod(type = MethodType.Update)
    int updateStudent(List<StudentUpdater> student);

    Student findStudent(StudentQuery student);

    long countStudentBy(StudentQuery student);

    List<Student> listStudentBy(StudentQuery student);

    StdPagedList<Student> stdPagedStudent(StdPagedQuery student);

    TagPagedList<Student> tagPagedStudent(TagPagedQuery hangzhou);

    Student findByUserName(@Entry("userName") String userName, @Entry("age") int[] ages);

    Student findByUserName(@Entry("userName") String userName, StudentQuery ages);
}