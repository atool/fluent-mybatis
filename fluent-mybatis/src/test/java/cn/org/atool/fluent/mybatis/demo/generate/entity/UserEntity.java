package cn.org.atool.fluent.mybatis.demo.generate.entity;

import cn.org.atool.fluent.mybatis.condition.interfaces.IEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.UserEntityHelper;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.UserMP;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.UserMP.Column;
import cn.org.atool.fluent.mybatis.annotation.IdType;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.annotation.TableName;
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
    @TableField(value = Column.gmt_created, insert = "now()")
    private Date gmtCreated;
    /**
     *
     */
    @TableField(value = Column.gmt_modified, update = "now()", insert = "now()")
    private Date gmtModified;
    /**
     *
     */
    @TableField(value = Column.is_deleted, insert = "0")
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