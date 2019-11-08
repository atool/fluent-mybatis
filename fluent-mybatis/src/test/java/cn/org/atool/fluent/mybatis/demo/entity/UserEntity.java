package cn.org.atool.fluent.mybatis.demo.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.demo.helper.UserEntityHelper;
import cn.org.atool.fluent.mybatis.demo.mapping.UserMP;
import cn.org.atool.fluent.mybatis.demo.mapping.UserMP.Column;
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
@TableName(UserMP.Table_Name)
public class UserEntity implements IEntity {
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
    @TableField(value = Column.address_id)
    private Long addressId;
    /**
     * 
     */
    @TableField(value = Column.age)
    private Integer age;
    /**
     * 
     */
    @TableField(value = Column.user_name)
    private String userName;
    /**
     * 
     */
    @TableField(value = Column.version)
    private String version;

    @Override
    public Serializable findPk() {
        return this.id;
    }

    /**
     * 将实体对象转换为map
     */
    @Override
    public Map<String, Object> toMap() {
        return UserEntityHelper.map(this);
    }
}
