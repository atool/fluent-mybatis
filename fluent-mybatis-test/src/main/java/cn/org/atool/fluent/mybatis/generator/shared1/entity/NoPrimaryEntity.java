package cn.org.atool.fluent.mybatis.generator.shared1.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * NoPrimaryEntity: 数据映射实体定义
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
    table = "no_primary",
    schema = "fluent_mybatis",
    mapperBeanPrefix = "new"
)
public class NoPrimaryEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableField("column_1")
  private Integer column1;

  @TableField("column_2")
  private String column2;

  @TableField("alias")
  private String alias;

  @Override
  public final Class entityClass() {
    return NoPrimaryEntity.class;
  }
}
