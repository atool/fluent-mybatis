package cn.org.atool.fluent.mybatis.generator.shared3.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.GmtCreate;
import cn.org.atool.fluent.mybatis.annotation.GmtModified;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.RefMethod;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * MemberEntity: 数据映射实体定义
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
    table = "t_member",
    schema = "fluent_mybatis"
)
public class MemberEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      desc = "主键id"
  )
  private Long id;

  @TableField(
      value = "age",
      desc = "年龄"
  )
  private Integer age;

  @TableField(
      value = "is_girl",
      desc = "0:男孩; 1:女孩"
  )
  private Boolean isGirl;

  @TableField(
      value = "school",
      desc = "学校"
  )
  private String school;

  @TableField(
      value = "user_name",
      desc = "名字"
  )
  private String userName;

  @TableField(
      value = "gmt_created",
      insert = "now()",
      desc = "创建时间"
  )
  @GmtCreate
  private Date gmtCreated;

  @TableField(
      value = "gmt_modified",
      insert = "now()",
      update = "now()",
      desc = "更新时间"
  )
  @GmtModified
  private Date gmtModified;

  @TableField(
      value = "is_deleted",
      insert = "0",
      desc = "是否逻辑删除"
  )
  @LogicDelete
  private Boolean isDeleted;

  @Override
  public final Class entityClass() {
    return MemberEntity.class;
  }

  /**
   * @see cn.org.atool.fluent.mybatis.generator.shared3.IEntityRelation#findMyFavoriteOfMemberEntity(List)
   */
  @RefMethod("isDeleted = isDeleted && memberId = id")
  public List<MemberFavoriteEntity> findMyFavorite() {
    return super.invoke("findMyFavorite", true);
  }

  /**
   * @see cn.org.atool.fluent.mybatis.generator.shared3.IEntityRelation#findExFriendsOfMemberEntity(MemberEntity)
   */
  @RefMethod
  public List<MemberEntity> findExFriends() {
    return super.invoke("findExFriends", true);
  }

  /**
   * @see cn.org.atool.fluent.mybatis.generator.shared3.IEntityRelation#findCurrFriendOfMemberEntity(MemberEntity)
   */
  @RefMethod
  public MemberEntity findCurrFriend() {
    return super.invoke("findCurrFriend", true);
  }
}
