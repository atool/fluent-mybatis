package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.functions.TableSupplier;
import java.io.Serializable;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Date;
import java.util.function.Consumer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * MemberLoveEntity: 数据映射实体定义
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
    table = "t_member_love",
    schema = "fluent_mybatis"
)
public class MemberLoveEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  /**
   * 主键id
   */
  @TableId("id")
  private Long id;

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
   * member表外键
   */
  @TableField("boy_id")
  private Long boyId;

  /**
   * member表外键
   */
  @TableField("girl_id")
  private Long girlId;

  /**
   * 创建时间
   */
  @TableField("gmt_created")
  private Date gmtCreated;

  /**
   * 状态
   */
  @TableField("status")
  private String status;

  @Override
  public Serializable findPk() {
    return this.id;
  }

  @Override
  public Consumer<Long> pkSetter() {
    return this::setId;
  }

  @Override
  public final Class<? extends IEntity> entityClass() {
    return MemberLoveEntity.class;
  }

  @Override
  public final MemberLoveEntity changeTableBelongTo(TableSupplier supplier) {
    return super.changeTableBelongTo(supplier);
  }

  @Override
  public final MemberLoveEntity changeTableBelongTo(String table) {
    return super.changeTableBelongTo(table);
  }
}
