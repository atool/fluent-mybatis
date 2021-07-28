package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.customize.MyEntity;
import java.io.Serializable;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * HomeAddressEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings("unchecked")
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@FluentMybatis(
    table = "home_address",
    mapperBeanPrefix = "my",
    defaults = MyCustomerInterface.class
)
public class HomeAddressEntity extends RichEntity implements MyEntity<HomeAddressEntity> {
  private static final long serialVersionUID = 1L;

  /**
   * 主键id
   */
  @TableId("id")
  private Long id;

  /**
   * 创建时间
   */
  @TableField(
      value = "gmt_created",
      insert = "now()"
  )
  private Date gmtCreated;

  /**
   * 更新时间
   */
  @TableField(
      value = "gmt_modified",
      insert = "now()",
      update = "now()"
  )
  private Date gmtModified;

  /**
   * 是否逻辑删除
   */
  @TableField(
      value = "is_deleted",
      insert = "0"
  )
  @LogicDelete
  private Boolean isDeleted;

  /**
   * 详细住址
   */
  @TableField("address")
  private String address;

  /**
   * 城市
   */
  @TableField("city")
  private String city;

  /**
   * 区
   */
  @TableField("district")
  private String district;

  /**
   * 数据隔离环境
   */
  @TableField("env")
  private String env;

  /**
   * 省份
   */
  @TableField("province")
  private String province;

  /**
   * 用户id
   */
  @TableField("student_id")
  private Long studentId;

  /**
   * 租户标识
   */
  @TableField("tenant")
  private Long tenant;

  @Override
  public Serializable findPk() {
    return this.id;
  }

  @Override
  public final Class<? extends IEntity> entityClass() {
    return HomeAddressEntity.class;
  }

  @Override
  public final HomeAddressEntity changeTableBelongTo(String table) {
    return super.changeTableBelongTo(table);
  }
}
