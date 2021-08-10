package cn.org.atool.fluent.mybatis.db.sqlite.entity;

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
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * SqliteStudentEntity: 数据映射实体定义
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
    table = "student",
    dbType = DbType.SQLITE
)
public class SqliteStudentEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  /**
   */
  @TableId("id")
  private Integer id;

  /**
   */
  @TableField("address")
  private String address;

  /**
   */
  @TableField("age")
  private Integer age;

  /**
   */
  @TableField("birthday")
  private String birthday;

  /**
   */
  @TableField("bonus_points")
  private Integer bonusPoints;

  /**
   */
  @TableField("desk_mate_id")
  private Integer deskMateId;

  /**
   */
  @TableField("email")
  private String email;

  /**
   */
  @TableField("env")
  private String env;

  /**
   */
  @TableField("gender")
  private Integer gender;

  /**
   */
  @TableField("gmt_created")
  private String gmtCreated;

  /**
   */
  @TableField("gmt_modified")
  private String gmtModified;

  /**
   */
  @TableField("grade")
  private Integer grade;

  /**
   */
  @TableField("home_address_id")
  private Integer homeAddressId;

  /**
   */
  @TableField("home_county_id")
  private Integer homeCountyId;

  /**
   */
  @TableField("is_deleted")
  private Integer isDeleted;

  /**
   */
  @TableField("phone")
  private String phone;

  /**
   */
  @TableField("status")
  private String status;

  /**
   */
  @TableField("tenant")
  private Integer tenant;

  /**
   */
  @TableField("user_name")
  private String userName;

  /**
   */
  @TableField("version")
  private String version;

  @Override
  public Serializable findPk() {
    return this.id;
  }

  @Override
  public final Class<? extends IEntity> entityClass() {
    return SqliteStudentEntity.class;
  }

  @Override
  public final SqliteStudentEntity changeTableBelongTo(TableSupplier supplier) {
    return super.changeTableBelongTo(supplier);
  }

  @Override
  public final SqliteStudentEntity changeTableBelongTo(String table) {
    return super.changeTableBelongTo(table);
  }
}