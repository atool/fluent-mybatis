package cn.org.atool.fluent.mybatis.generator.shared3.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * MemberFavoriteEntity: 数据映射实体定义
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
    table = "t_member_favorite",
    schema = "fluent_mybatis",
    desc = "成员爱好"
)
public class MemberFavoriteEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      desc = "主键id"
  )
  private Long id;

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
      value = "favorite",
      desc = "爱好: 电影, 爬山, 徒步..."
  )
  private String favorite;

  @TableField(
      value = "gmt_created",
      desc = "创建时间"
  )
  private Date gmtCreated;

  @TableField(
      value = "member_id",
      desc = "member表外键"
  )
  private Long memberId;

  @Override
  public final Class entityClass() {
    return MemberFavoriteEntity.class;
  }
}
