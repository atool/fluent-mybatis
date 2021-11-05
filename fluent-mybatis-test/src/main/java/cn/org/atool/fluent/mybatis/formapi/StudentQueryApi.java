package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.FormEntry;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

import static cn.org.atool.fluent.form.annotation.EntryType.Between;
import static cn.org.atool.fluent.form.annotation.EntryType.Form;

@SuppressWarnings("all")
@RestController
@FormService(table = "student")
public interface StudentQueryApi {
    @PostMapping("/byQuery")
    Student findStudentBy(@RequestBody StudentQuery student);

    @GetMapping("/byName")
    Student findByUserName(@RequestParam("userName") @FormEntry(name = "userName") String userName);

    Student findByUserName(@FormEntry(name = "userName") String userName, @FormEntry(name = "age", type = Between) int[] ages);

    Student findByUserName(@FormEntry(name = "userName") String userName, @FormEntry(type = Form) StudentQuery student);

    long countStudentBy(StudentQuery student);

    List<Student> listStudentBy(StudentQuery student);

    StdPagedList<Student> stdPagedStudent(StdPagedQuery student);

    TagPagedList<Student> tagPagedStudent(TagPagedQuery hangzhou);

    @Data
    @Accessors(chain = true)
    class StudentQuery implements Serializable {
        private String userName;

        @FormEntry(type = EntryType.LikeLeft)
        private String address;

        @FormEntry(type = Between)
        private int[] age;

        private Integer gender;
    }

    @Data
    @Accessors(chain = true)
    class TagPagedQuery extends StudentQuery {
        @FormEntry(type = EntryType.PagedTag)
        private int nextId;

        @FormEntry(type = EntryType.PageSize)
        private int pageSize = 10;
    }

    @Data
    @Accessors(chain = true)
    class Student implements Serializable {

        private String userName;

        private String status;

        private String phone;

        @FormEntry(name = "email")
        private String hisEmail;

        private Integer age;

        private String address;
    }
}