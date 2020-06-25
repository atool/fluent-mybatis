package cn.org.atool.fluent.mybatis.tutorial.entity;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.annotation.TableName;
import cn.org.atool.fluent.mybatis.base.IEntity;
import java.util.Date;

import cn.org.atool.fluent.mybatis.tutorial.helper.ReceivingAddressEntityHelper;

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
@TableName("receiving_address")
public class ReceivingAddressEntity implements IEntity{
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
     * 城市
     */
    @TableField(value = "city")
    private String city;
    /**
     * 详细住址
     */
    @TableField(value = "detail_address")
    private String detailAddress;
    /**
     * 区
     */
    @TableField(value = "district")
    private String district;
    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;
    /**
     * 省份
     */
    @TableField(value = "province")
    private String province;
    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    @Override
    public Serializable findPk() {
        return id;
    }

    @Override
    public Map<String, Object> toMap() {
        return ReceivingAddressEntityHelper.map(this);
    }

    @Override
    public Map<String, Object> columnMap() {
        return ReceivingAddressEntityHelper.columnMap(this);
    }
}