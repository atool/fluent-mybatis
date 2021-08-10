package cn.org.atool.fluent.mybatis.db.pg.entity;

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
 * PgStudentEntity: 数据映射实体定义
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
    schema = "test",
    dbType = DbType.POSTGRE_SQL
)
public class PgStudentEntity extends RichEntity {
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
  private Date birthday;

  /**
   */
  @TableField("bonus_points")
  private Long bonusPoints;

  /**
   */
  @TableField("desk_mate_id")
  private Long deskMateId;

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
  private Date gmtCreated;

  /**
   */
  @TableField("gmt_modified")
  private Date gmtModified;

  /**
   */
  @TableField("grade")
  private Integer grade;

  /**
   */
  @TableField("home_address_id")
  private Long homeAddressId;

  /**
   */
  @TableField("home_county_id")
  private Long homeCountyId;

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
  private Long tenant;

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
    return PgStudentEntity.class;
  }

  @Override
  public final PgStudentEntity changeTableBelongTo(TableSupplier supplier) {
    return super.changeTableBelongTo(supplier);
  }

  @Override
  public final PgStudentEntity changeTableBelongTo(String table) {
    return super.changeTableBelongTo(table);
  }
}