package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.annotation.Version;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import java.io.Serializable;
import java.lang.Class;
import java.lang.Long;
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

  /**
   */
  @TableId(
      value = "id",
      auto = false,
      seqName = "SELECT LAST_INSERT_ID() AS ID"
  )
  private String id;

  /**
   */
  @TableField("column_1")
  private String column1;

  /**
   */
  @TableField(
      value = "lock_version",
      insert = "0",
      update = "`lock_version` + 1"
  )
  @Version
  private Long lockVersion;

  @Override
  public Serializable findPk() {
    return this.id;
  }

  @Override
  public final Class<? extends IEntity> entityClass() {
    return NoAutoIdEntity.class;
  }
}
