package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * NoPrimaryEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@FluentMybatis(
    table = "no_primary",
    mapperBeanPrefix = "new"
)
public class NoPrimaryEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  /**
   */
  @TableField("column_1")
  private Integer column1;

  /**
   */
  @TableField("column_2")
  private String column2;

  @Override
  public final Class<? extends IEntity> entityClass() {
    return NoPrimaryEntity.class;
  }
}
