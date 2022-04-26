package cn.org.atool.fluent.mybatis.generator.shared2.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.customize.MyEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
      value = "province",
      desc = "省份"
  )
  private String province;

  @TableField(
      value = "student_id",
      desc = "用户id"
  )
  private Long studentId;

  @Override
  public final Class entityClass() {
    return HomeAddressEntity.class;
  }
}
