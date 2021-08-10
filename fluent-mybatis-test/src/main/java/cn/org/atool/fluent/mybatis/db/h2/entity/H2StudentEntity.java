package cn.org.atool.fluent.mybatis.db.h2.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.functions.TableSupplier;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import java.io.Serializable;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * H2StudentEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"unchecked"})
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@FluentMybatis(
    table = "STUDENT",
    dbType = DbType.H2
)
public class H2StudentEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  /**
   */
  @TableId("ID")
  private Long id;

  /**
   */
  @TableField("ADDRESS")
  private String address;

  /**
   */
  @TableField("AGE")
  private Integer age;

  /**
   */
  @TableField("BIRTHDAY")
  private Date birthday;

  /**
   */
  @TableField("BONUS_POINTS")
  private Long bonusPoints;

  /**
   */
  @TableField("DESK_MATE_ID")
  private Long deskMateId;

  /**
   */
  @TableField("EMAIL")
  private String email;

  /**
   */
  @TableField("ENV")
  private String env;

  /**
   */
  @TableField("GENDER")
  private Integer gender;

  /**
   */
  @TableField("GMT_CREATED")
  private Date gmtCreated;

  /**
   */
  @TableField("GMT_MODIFIED")
  private Date gmtModified;

  /**
   */
  @TableField("GRADE")
  private Integer grade;

  /**
   */
  @TableField("HOME_ADDRESS_ID")
  private Long homeAddressId;

  /**
   */
  @TableField("HOME_COUNTY_ID")
  private Long homeCountyId;

  /**
   */
  @TableField("IS_DELETED")
  private Integer isDeleted;

  /**
   */
  @TableField("PHONE")
  private String phone;

  /**
   */
  @TableField("STATUS")
  private String status;

  /**
   */
  @TableField("TENANT")
  private Long tenant;

  /**
   */
  @TableField("USER_NAME")
  private String userName;

  /**
   */
  @TableField("VERSION")
  private String version;

  @Override
  public Serializable findPk() {
    return this.id;
  }

  @Override
  public final Class<? extends IEntity> entityClass() {
    return H2StudentEntity.class;
  }

  @Override
  public final H2StudentEntity changeTableBelongTo(TableSupplier supplier) {
    return super.changeTableBelongTo(supplier);
  }

  @Override
  public final H2StudentEntity changeTableBelongTo(String table) {
    return super.changeTableBelongTo(table);
  }
}