package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.IEntity;
import java.util.Date;


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
@FluentMybatis(table = "address")
public class AddressEntity implements IEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 
     */
    @TableField(value = "address")
    private String address;
    /**
     * 
     */
    @TableField(value = "gmt_created")
    private Date gmtCreated;
    /**
     * 
     */
    @TableField(value = "gmt_modified")
    private Date gmtModified;
    /**
     * 
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;
    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

    @Override
    public Serializable findPk() {
        return id;
    }
}