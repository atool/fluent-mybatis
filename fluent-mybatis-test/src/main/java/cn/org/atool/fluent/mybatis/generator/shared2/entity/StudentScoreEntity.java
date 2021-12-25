package cn.org.atool.fluent.mybatis.generator.shared2.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.RefMethod;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.customize.MyEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * StudentScoreEntity: 数据映射实体定义
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
@Builder
@FluentMybatis(
    table = "student_score",
    schema = "fluent_mybatis",
    mapperBeanPrefix = "my",
    defaults = MyCustomerInterface.class
)
public class StudentScoreEntity extends RichEntity implements MyEntity<StudentScoreEntity> {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      desc = "主键ID"
  )
  private Long id;

  @TableField(
      value = "gmt_created",
      insert = "now()",
      desc = "记录创建时间"
  )
  private Date gmtCreated;

  @TableField(
      value = "gmt_modified",
      insert = "now()",
      update = "now()",
      desc = "记录最后修改时间"
  )
  private Date gmtModified;

  @TableField(
      value = "is_deleted",
      insert = "0",
      desc = "逻辑删除标识"
  )
  @LogicDelete
  private Boolean isDeleted;

  @TableField(
      value = "env",
      desc = "数据隔离环境"
  )
  private String env;

  @TableField(
      value = "gender",
      desc = "性别, 0:女; 1:男"
  )
  private Integer gender;

  @TableField(
      value = "school_term",
      desc = "学期"
  )
  private Integer schoolTerm;

  @TableField(
      value = "score",
      desc = "成绩"
  )
  private Integer score;

  @TableField(
      value = "student_id",
      desc = "学号"
  )
  private Long studentId;

  @TableField(
      value = "subject",
      desc = "学科"
  )
  private String subject;

  @TableField(
      value = "tenant",
      desc = "租户标识"
  )
  private Long tenant;

  @Override
  public final Class entityClass() {
    return StudentScoreEntity.class;
  }

  /**
   * @see cn.org.atool.fluent.mybatis.generator.shared2.IEntityRelation#findStudentOfStudentScoreEntity(java.util.List)
   */
  @RefMethod("isDeleted = isDeleted && id = studentId && env = env")
  public StudentEntity findStudent() {
    return super.invoke("findStudent", true);
  }
}
