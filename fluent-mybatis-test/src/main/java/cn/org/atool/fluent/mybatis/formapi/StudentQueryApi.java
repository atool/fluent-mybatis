package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("all")
@FormService(entityTable = "student")
public interface StudentQueryApi {
    Student findStudentBy(StudentQuery student);

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

        @Entry(type = EntryType.Between)
        private int[] age;

        private Integer gender;
    }

    @Data
    @Accessors(chain = true)
    class StdPagedQuery extends StudentQuery {
        @Entry(type = EntryType.PageSize)
        private int pageSize = 10;

        @Entry(type = EntryType.CurrPage)
        private int currPage = 0;
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

        @Entry("email")
        private String hisEmail;

        private Integer age;

        private String address;
    }
}