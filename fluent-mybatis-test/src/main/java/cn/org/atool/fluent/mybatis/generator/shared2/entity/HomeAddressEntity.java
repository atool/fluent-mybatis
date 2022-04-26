package cn.org.atool.fluent.mybatis.generator.shared2.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.customize.MyEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * HomeAddressEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@AllArgsConstructor
@NoArgsConstructor
@FluentMybatis(
    table = "home_address",
    schema = "fluent_mybatis",
    mapperBeanPrefix = "my",
    defaults = MyCustomerInterface.class
)
public class HomeAddressEntity extends MyEntity<HomeAddressEntity> {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      desc = "主键id"
  )
  private Long id;

  @TableField(
      value = "gmt_created",
      insert = "now()",
      desc = "创建时间"
  )
  private Date gmtCreated;

  @TableField(
      value = "gmt_modified",
      insert = "now()",
      update = "now()",
      desc = "更新时间"
  )
  private Date gmtModified;

  @TableField(
      value = "is_deleted",
      insert = "0",
      desc = "是否逻辑删除"
  )
  @LogicDelete
  private Boolean isDeleted;

  @TableField(
      value = "address",
      desc = "详细住址"
  )
  private String address;

  @TableField(
      value = "city",
      desc = "城市"
  )
  private String city;

  @TableField(
      value = "district",
      desc = "区"
  )
  private String district;

  @TableField(
      value = "env",
      desc = "数据隔离环境"
  )
  private String env;

  @TableField(
      value = "province",
      desc = "省份"
  )
  private String province;

  @TableField(
      value = "student_id",
      desc = "用户id"
  )
  private Long studentId;

  @TableField(
      value = "tenant",
      desc = "租户标识"
  )
  private Long tenant;

  @Override
  public final Class entityClass() {
    return HomeAddressEntity.class;
  }
}
