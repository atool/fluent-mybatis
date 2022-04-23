package cn.org.atool.fluent.mybatis.formservice.service;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.mybatis.formservice.model.*;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import lombok.ToString;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Result;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Observable;

import static org.apache.ibatis.type.JdbcType.DATE;

@SuppressWarnings("all")
@FormService(entity = StudentEntity.class, proxy = false)
public interface StudentService {
    /**
     * 保存单个学生信息
     */
    @Transactional(label = {"a", "b"})
    Student saveStudent(@NotNull Student student);

    /**
     * 批量保存单个学生信息
     */
    boolean saveStudent(List<Student> students);

    /**
     * 更新学生信息
     */
    @ConstructorArgs(value = {
        @Arg(javaType = NoSuchMethodException.class),
        @Arg(javaType = Observable.class)
    })
    int updateStudent(StudentUpdater student);

    /**
     * 按学号物理删除学生信息
     */
    @Result(javaType = ToString.class, jdbcType = DATE)
    int deleteById(int... ids);

    int logicDeleteStudent(StudentQuery student);

    int updateStudent(List<StudentUpdater> student);

    Student findStudent(StudentQuery student);

    long countStudentBy(StudentQuery student);

    List<Student> listStudentBy(StudentQuery student);

    StdPagedList<Student> stdPagedStudent(StdPagedQuery student);

    TagPagedList<Student> tagPagedStudent(TagPagedQuery hangzhou);

    Student findByUserName(@Entry("userName") String userName, @Entry("age") int[] ages);

    Student findByUserName(@Entry("userName") String userName, StudentQuery ages);
}