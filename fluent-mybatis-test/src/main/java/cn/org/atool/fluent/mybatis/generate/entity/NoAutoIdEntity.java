package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.entity.RichEntity;
import java.io.Serializable;
import java.lang.Override;
import java.lang.String;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * NoAutoIdEntity: 数据映射实体定义
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
    table = "no_auto_id",
    mapperBeanPrefix = "new"
)
public class NoAutoIdEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      auto = false,
      seqName = "SELECT LAST_INSERT_ID() AS ID"
  )
  private String id;

  @TableField("column_1")
  private String column1;

  @Override
  public Serializable findPk() {
    return this.id;
  }
}
