package cn.org.atool.fluent.mybatis.formservice.restapi;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.FormMethod;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.form.annotation.MethodType;
import cn.org.atool.fluent.mybatis.formservice.model.StdPagedQuery;
import cn.org.atool.fluent.mybatis.formservice.model.Student;
import cn.org.atool.fluent.mybatis.formservice.model.StudentQuery;
import cn.org.atool.fluent.mybatis.formservice.model.TagPagedQuery;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.form.annotation.EntryType.Between;
import static cn.org.atool.fluent.form.annotation.EntryType.Form;

@SuppressWarnings("all")
@RestController
@FormService(table = "student", proxy = false)
public interface StudentRestApi {
    @PostMapping(value = "/save", name = "保存学生信息")
    @FormMethod(type = MethodType.Save)
    Student save(Student student);

    @PostMapping(value = "/findStudentBy", name = "根据条件查询学习信息")
    Student findStudentBy(@RequestBody StudentQuery student);

    @GetMapping("/byName")
    Student findByUserName(@RequestParam("userName")
                           @Entry(value = "userName") String userName);

    @PostMapping("/findByUserName")
    Student findByUserName(@Entry(value = "userName") String userName,
                           @Entry(value = "age", type = Between) int[] ages);

    @PostMapping("/findByUserName2")
    Student findByUserName(@Entry(value = "userName") String userName,
                           @Entry(type = Form) StudentQuery student);

    @PostMapping("/countStudentBy")
    long countStudentBy(StudentQuery student);

    @PostMapping("/listStudentBy")
    List<Student> listStudentBy(StudentQuery student);

    @PostMapping("/stdPagedStudent")
    StdPagedList<Student> stdPagedStudent(StdPagedQuery student);

    @PostMapping("/tagPagedStudent")
    TagPagedList<Student> tagPagedStudent(TagPagedQuery hangzhou);

    @GetMapping("/hello")
    default Map testGet() {
        return new HashMap<String, String>() {{
            put("name", "springboot");
        }};
    }
}