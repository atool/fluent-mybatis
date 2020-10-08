package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.IEntity;
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
@FluentMybatis(table = "address", mapperBeanPrefix = "my",
	daoInterface = {MyCustomerInterface.class})
public class AddressEntity implements IEntity, IBaseEntity{
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
    @TableField(value = "address")
    private String address;
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