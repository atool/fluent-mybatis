package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * IdcardEntity: 数据映射实体定义
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
    table = "idcard"
)
public class IdcardEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  /**
   * 主键id
   */
  @TableId("id")
  private Long id;

  /**
   * 是否逻辑删除
   */
  @TableField(
      value = "is_deleted",
      insert = "0"
  )
  @LogicDelete
  private Long isDeleted;

  /**
   */
  @TableField("code")
  private String code;

  /**
   * 版本锁
   */
  @TableField(
      value = "version",
      update = "version + 1"
  )
  private Long version;

  @Override
  public Serializable findPk() {
    return this.id;
  }

  @Override
  public final Class<? extends IEntity> entityClass() {
    return IdcardEntity.class;
  }
}
