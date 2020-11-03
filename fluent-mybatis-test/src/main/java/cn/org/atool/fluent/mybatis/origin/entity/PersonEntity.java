package cn.org.atool.fluent.mybatis.origin.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class PersonEntity {
    private Integer id;
    private String name;
    private Integer age;
    // 个人身份证关联
    private IdCardEntity card;

    private List<NickNameEntity> nickNames;
}