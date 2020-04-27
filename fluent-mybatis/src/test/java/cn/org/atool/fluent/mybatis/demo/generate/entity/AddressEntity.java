package cn.org.atool.fluent.mybatis.demo.generate.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.AddressEntityHelper;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP.Column;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
@Accessors(chain = true)
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
    @TableField(value = Column.gmt_created, update = "now()", fill = FieldFill.INSERT)
    private Date gmtCreated;
    /**
     * 
     */
    @TableField(value = Column.gmt_modified, update = "now()", fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
    /**
     * 
     */
    @TableField(value = Column.is_deleted, update = "0", fill = FieldFill.INSERT)
    private Boolean isDeleted;
    /**
     * 
     */
    @TableField(value = Column.address)
    private String address;

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
