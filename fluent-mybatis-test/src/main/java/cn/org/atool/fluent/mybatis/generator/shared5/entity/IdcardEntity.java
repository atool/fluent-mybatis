package cn.org.atool.fluent.mybatis.generator.shared5.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.annotation.Version;
import cn.org.atool.fluent.mybatis.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * IdcardEntity: 数据映射实体定义
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
    table = "idcard",
    schema = "fluent_mybatis",
    useCached = true
)
public class IdcardEntity extends BaseEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      auto = false,
      seqName = "SELECT NEXTVAL('testSeq')",
      desc = "主键id"
  )
  private Long id;

  @TableField("code")
  private String code;

  @TableField(
      value = "version",
      insert = "0",
      update = "`version` + 1",
      desc = "版本锁"
  )
  @Version
  private Long version;

  @TableField(
      value = "is_deleted",
      insert = "0",
      desc = "是否逻辑删除"
  )
  @LogicDelete
  private Long isDeleted;

  @Override
  public final Class entityClass() {
    return IdcardEntity.class;
  }
}
