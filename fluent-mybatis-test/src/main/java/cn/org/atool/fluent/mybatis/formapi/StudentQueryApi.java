package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentScoreEntity;
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
    Student findByUserName(@RequestParam("userName") @Entry(name = "userName") String userName);

    Student findByUserName(@Entry(name = "userName") String userName, @Entry(name = "age", type = Between) int[] ages);

    Student findByUserName(@Entry(name = "userName") String userName, @Entry(type = Form) StudentQuery student);

    long countStudentBy(StudentQuery student);

    List<Student> listStudentBy(StudentQuery student);

    StdPagedList<Student> stdPagedStudent(StdPagedQuery student);

    TagPagedList<Student> tagPagedStudent(TagPagedQuery hangzhou);

    @Data
    @Accessors(chain = true)
    class StudentQuery implements Serializable {
        private String userName;

        @Entry(type = EntryType.LikeLeft)
        private String address;

        @Entry(type = Between)
        private Integer[] age;

        private Integer gender;
        /**
         * 默认正序
         */
        @Entry(type = EntryType.OrderBy, name = "userName")
        private boolean byUserName = true;
        /**
         * 默认倒序
         */
        @Entry(type = EntryType.OrderBy, name = "age")
        private boolean byAge;
    }

    @Data
    @Accessors(chain = true)
    class TagPagedQuery extends StudentQuery {
        @Entry(type = EntryType.PagedTag)
        private int nextId;

        @Entry(type = EntryType.PageSize)
        private int pageSize = 10;
    }

    @Data
    @Accessors(chain = true)
    class Student implements Serializable {

        private String userName;

        private String status;

        private String phone;

        @Entry(name = "email")
        private String hisEmail;

        private Integer age;

        private String address;
        /**
         * 同桌
         */
        private Student deskMate;

        @Entry(name = "findStudentScoreList")
        private List<Score> scores;

        private StudentScoreEntity englishScore;
    }

    @Data
    public static class Score {
        private Integer score;

        private String subject;
    }
}