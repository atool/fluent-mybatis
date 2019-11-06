package cn.org.atool.fluent.mybatis.demo.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.demo.helper.AddressEntityHelper;
import cn.org.atool.fluent.mybatis.demo.mapping.AddressMP;
import cn.org.atool.fluent.mybatis.demo.mapping.AddressMP.Column;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.Date;
import java.io.Serializable;

import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author generate code
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(AddressMP.Table_Name)
public class AddressEntity implements IEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = Column.id, type = IdType.AUTO)
    private Long id;
    /**
     * 
     */
    @TableField(value = Column.address)
    private String address;
    /**
     * 
     */
    @TableField(value = Column.is_deleted)
    private Boolean isDeleted;
    /**
     * 
     */
    @TableField(value = Column.gmt_created, update = "now()", fill = FieldFill.INSERT)
    private Date gmtCreated;
    /**
     * 
     */
    @TableField(value = Column.gmt_modified, update = "now()", fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @Override
    public Serializable findPk() {
        return this.id;
    }

    /**
     * 将实体对象转换为map
     */
    @Override
    public Map<String, Object> toMap() {
        return AddressEntityHelper.map(this);
    }
}
