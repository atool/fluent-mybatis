package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.*;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.customize.IBaseEntity;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.generate.helper.UserEntityHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
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
@FluentMybatis(
    prefix = "t_",
    daoInterface = @DaoInterface(value = MyCustomerInterface.class, args = {ParaType.Entity})
)
public class UserEntity implements IEntity, IBaseEntity<UserEntity> {
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

    @Override
    public Map<String, Object> toMap() {
        return UserEntityHelper.map(this);
    }

    @Override
    public Map<String, Object> columnMap() {
        return UserEntityHelper.columnMap(this);
    }
}