package cn.org.atool.fluent.mybatis.formservice.model;


import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentScoreEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Student {
    private Long id;

    private String userName;

    private String status;

    private String phone;

    @Entry("email")
    private String hisEmail;

    private Integer age;

    private String address;
    /**
     * 同桌
     */
    private Student deskMate;

    @Entry(value = "findStudentScoreList")
    private List<Score> scores;

    private StudentScoreEntity englishScore;
}