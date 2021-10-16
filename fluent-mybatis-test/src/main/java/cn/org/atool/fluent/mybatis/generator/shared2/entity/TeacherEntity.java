package cn.org.atool.fluent.mybatis.generator.shared2.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.RefMethod;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * TeacherEntity: 数据映射实体定义
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
@FluentMybatis(
    table = "teacher",
    schema = "fluent_mybatis"
)
public class TeacherEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  /**
   * 主键id
   */
  @TableId("id")
  private Long id;

  /**
   * 创建时间
   */
  @TableField(
      value = "gmt_created",
      insert = "now()"
  )
  private Date gmtCreated;

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
   * 数据隔离环境
   */
  @TableField("env")
  private String env;

  /**
   * 名字
   */
  @TableField("user_name")
  private String userName;

  @Override
  public final Class entityClass() {
    return TeacherEntity.class;
  }

  /**
   * 实现 {@link cn.org.atool.fluent.mybatis.generator.shared2.IEntityRelation#findStudentListOfTeacherEntity(TeacherEntity)}
   */
  @RefMethod
  public List<StudentEntity> findStudentList() {
    return super.invoke("findStudentList", true);
  }
}
