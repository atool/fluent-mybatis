package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.customize.IBaseEntity;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import java.io.Serializable;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * UserEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@Data
@Accessors(
    chain = true
)
@FluentMybatis(
    table = "t_user",
    mapperBeanPrefix = "my",
    daoInterface = {MyCustomerInterface.class}
)
public class UserEntity implements IEntity, IBaseEntity<UserEntity> {
  private static final long serialVersionUID = 1L;

  @TableId("id")
  private Long id;

  @TableField(
      value = "gmt_created",
      insert = "now()"
  )
  private Date gmtCreated;

  @TableField(
      value = "gmt_modified",
      insert = "now()",
      update = "now()"
  )
  private Date gmtModified;

  @TableField(
      value = "is_deleted",
      insert = "0"
  )
  private Boolean isDeleted;

  @TableField("address_id")
  private Long addressId;

  @TableField("age")
  private Integer age;

  @TableField("e_mail")
  private String eMail;

  @TableField("first_name")
  private String firstName;

  @TableField("grade")
  private Integer grade;

  @TableField("last_name")
  private String lastName;

  @TableField("post_code")
  private String postCode;

  @TableField("user_name")
  private String userName;

  @TableField(
      value = "version",
      notLarge = false
  )
  private String version;

  @Override
  public Serializable findPk() {
    return this.id;
  }
}
