package cn.org.atool.fluent.mybatis.formservice.service;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.mybatis.formservice.model.*;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("all")
@FormService(entity = StudentEntity.class, proxy = false)
public interface StudentService {
    /**
     * 保存单个学生信息
     */
    @Transactional(label = {"a", "b"})
    Student saveStudent(Student student);

    /**
     * 批量保存单个学生信息
     */
    boolean saveStudent(List<Student> students);

    /**
     * 更新学生信息
     */
    int updateStudent(StudentUpdater student);

    /**
     * 按学号物理删除学生信息
     */
    int deleteById(int... ids);

    int logicDeleteStudent(StudentQuery student);

    int updateStudent(List<StudentUpdater> student);

    Student findStudent(StudentQuery student);

    @Cached(key = "#args[0].userName", cacheType = CacheType.LOCAL)
    long countStudentBy(StudentQuery student);

    List<Student> listStudentBy(StudentQuery student);

    StdPagedList<Student> stdPagedStudent(StdPagedQuery student);

    TagPagedList<Student> tagPagedStudent(TagPagedQuery hangzhou);

    Student findByUserName(@Entry("userName") String userName, @Entry("age") int[] ages);

    Student findByUserName(@Entry("userName") String userName, StudentQuery ages);
}