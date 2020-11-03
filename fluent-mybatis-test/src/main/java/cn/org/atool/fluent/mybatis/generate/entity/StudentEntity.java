package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.*;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.customize.IBaseEntity;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * StudentEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@Data
@Accessors(
    chain = true
)
@FluentMybatis(
    table = "t_student",
    mapperBeanPrefix = "my",
    defaults = MyCustomerInterface.class
)
public class StudentEntity implements IEntity, IBaseEntity<StudentEntity> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;

    /**
     * 创建时间
     */
    @TableField(
        value = "gmt_created",
        insert = "now()"
    )
    private Date gmtCreated;

    /**
     * 更新时间
     */
    @TableField(
        value = "gmt_modified",
        insert = "now()",
        update = "now()"
    )
    private Date gmtModified;

    /**
     * 是否逻辑删除
     */
    @TableField(
        value = "is_deleted",
        insert = "0"
    )
    private Boolean isDeleted;

    /**
     * 账号
     */
    @TableField("account")
    private String account;

    /**
     * 地址id
     */
    @TableField("address_id")
    private Long addressId;

    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 生日
     */
    @TableField("birthday")
    private Date birthday;

    /**
     * 积分
     */
    @TableField("bonus_points")
    private Long bonusPoints;

    /**
     * 电子邮件
     */
    @TableField("e_mail")
    private String eMail;

    /**
     * 数据隔离环境
     */
    @TableField("env")
    private String env;

    /**
     * 年级
     */
    @TableField("grade")
    private Integer grade;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 状态(字典)
     */
    @TableField("status")
    private String status;

    /**
     * 租户标识
     */
    @TableField("tenant")
    private Long tenant;

    /**
     * 名字
     */
    @TableField("user_name")
    private String userName;

    /**
     * 版本号
     */
    @TableField(
        value = "version",
        notLarge = false
    )
    private String version;

    @RefEntity(value = {"id:studentId", "env:env", "isDeleted:isDeleted"})
    @NotField
    private List<StudentScoreEntity> studentScoreList;

    @Override
    public Serializable findPk() {
        return this.id;
    }
}