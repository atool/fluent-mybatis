package cn.org.atool.fluent.mybatis.origin.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
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

    @Getter(AccessLevel.NONE)
    private List<NickNameEntity> nickNames;

    public List<NickNameEntity> getNickNames() {
        System.out.println("------------");
        return nickNames;
    }
}