package cn.org.atool.fluent.mybatis.generator.shared2.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.RefMethod;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.customize.MyEntity;
import lombok.AllArgsConstructor;
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
@FluentMybatis(
    table = "student_score",
    schema = "fluent_mybatis",
    mapperBeanPrefix = "my",
    defaults = MyCustomerInterface.class
)
public class StudentScoreEntity extends MyEntity<StudentScoreEntity> {
  private static final long serialVersionUID = 1L;

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
