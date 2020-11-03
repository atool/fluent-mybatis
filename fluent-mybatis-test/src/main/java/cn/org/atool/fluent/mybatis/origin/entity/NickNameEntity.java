package cn.org.atool.fluent.mybatis.origin.entity;

import lombok.Data;

@Data
public class NickNameEntity {
    private Long id;
    private String nickName;
    private Long personId;
}