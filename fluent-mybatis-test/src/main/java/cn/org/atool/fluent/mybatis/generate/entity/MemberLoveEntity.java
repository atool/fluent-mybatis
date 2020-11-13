package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.impl.RichEntity;
import java.io.Serializable;
import java.lang.Boolean;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * MemberLoveEntity: 数据映射实体定义
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
    table = "t_member_love"
)
public class MemberLoveEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId("id")
  private Long id;

  @TableField(
      value = "gmt_modified",
      insert = "now()",
      update = "now()"
  )
  private Date gmtModified;

  @TableField(
      value = "is_deleted",
      insert = "0"
  )
  private Boolean isDeleted;

  @TableField("boy_id")
  private Long boyId;

  @TableField("girl_id")
  private Long girlId;

  @TableField("gmt_created")
  private Date gmtCreated;

  @TableField("status")
  private String status;

  @Override
  public Serializable findPk() {
    return this.id;
  }
}
