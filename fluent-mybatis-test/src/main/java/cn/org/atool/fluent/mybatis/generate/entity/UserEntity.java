package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.IEntity;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Date;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.customize.IBaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Powered by FluentMyBatis
 */
@Getter
@Setter
@Accessors(chain = true)
@FluentMybatis(table = "t_user", mapperBeanPrefix = "my",
	daoInterface = {MyCustomerInterface.class})
public class UserEntity implements IEntity, IBaseEntity{
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
    @TableField(value = "grade")
    private Integer grade;
    /**
     * 
     */
    @TableField(value = "user_name")
    private String userName;
    /**
     * 
     */
    @TableField(value = "version", notLarge = false)
    private String version;

    @Override
    public Serializable findPk() {
        return id;
    }
}