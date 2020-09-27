package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.annotation.TableName;
import cn.org.atool.fluent.mybatis.base.IEntity;
import java.util.Date;
import cn.org.atool.fluent.mybatis.customize.IBaseEntity;
import cn.org.atool.fluent.mybatis.generate.helper.AddressEntityHelper;

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
@TableName("address")
public class AddressEntity implements IEntity, IBaseEntity<AddressEntity>{
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

    @Override
    public Map<String, Object> toMap() {
        return AddressEntityHelper.map(this);
    }

    @Override
    public Map<String, Object> columnMap() {
        return AddressEntityHelper.columnMap(this);
    }
}