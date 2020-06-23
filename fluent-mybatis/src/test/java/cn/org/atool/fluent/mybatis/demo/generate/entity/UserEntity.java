package cn.org.atool.fluent.mybatis.demo.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.annotation.TableName;
import cn.org.atool.fluent.mybatis.base.IEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.Map;
import java.io.Serializable;

import java.util.Date;
import cn.org.atool.fluent.mybatis.demo.IBaseEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.UserEntityHelper;

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
@TableName("t_user")
public class UserEntity implements IEntity, IBaseEntity<UserEntity>{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 
     */
    @TableField(value = "gmt_created", insert = "now()")
    private Date gmtCreated;
    /**
     * 
     */
    @TableField(value = "gmt_modified", insert = "now()", update = "now()")
    private Date gmtModified;
    /**
     * 
     */
    @TableField(value = "is_deleted", insert = "0")
    private Boolean isDeleted;
    /**
     * 
     */
    @TableField(value = "address_id")
    private Long addressId;
    /**
     * 
     */
    @TableField(value = "age")
    private Integer age;
    /**
     * 
     */
    @TableField(value = "user_name")
    private String userName;
    /**
     * 
     */
    @TableField(value = "version")
    private String version;

    @Override
    public Serializable findPk() {
        return id;
    }

    /**
     * 将实体对象转换为map
     */
    @Override
    public Map<String, Object> toMap() {
        return UserEntityHelper.map(this);
    }
}