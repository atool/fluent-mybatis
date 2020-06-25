package cn.org.atool.fluent.mybatis.tutorial.entity;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.annotation.TableName;
import cn.org.atool.fluent.mybatis.base.IEntity;
import java.util.Date;

import cn.org.atool.fluent.mybatis.tutorial.helper.UserEntityHelper;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author generate code
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user")
public class UserEntity implements IEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified", insert = "now()", update = "now()")
    private Date gmtModified;
    /**
     * 是否逻辑删除
     */
    @TableField(value = "is_deleted", insert = "0")
    private Boolean isDeleted;
    /**
     * 账号
     */
    @TableField(value = "account")
    private String account;
    /**
     * 头像
     */
    @TableField(value = "avatar")
    private String avatar;
    /**
     * 生日
     */
    @TableField(value = "birthday")
    private Date birthday;
    /**
     * 电子邮件
     */
    @TableField(value = "e_mail")
    private String eMail;
    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;
    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;
    /**
     * 电话
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 状态(字典)
     */
    @TableField(value = "status")
    private String status;
    /**
     * 名字
     */
    @TableField(value = "user_name")
    private String userName;

    @Override
    public Serializable findPk() {
        return id;
    }

    @Override
    public Map<String, Object> toMap() {
        return UserEntityHelper.map(this);
    }

    @Override
    public Map<String, Object> columnMap() {
        return UserEntityHelper.columnMap(this);
    }
}