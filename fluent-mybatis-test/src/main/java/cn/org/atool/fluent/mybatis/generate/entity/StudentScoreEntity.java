package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.RefMethod;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.customize.MyEntity;
import java.io.Serializable;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * StudentScoreEntity: 数据映射实体定义
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
    table = "student_score",
    mapperBeanPrefix = "my",
    defaults = MyCustomerInterface.class
)
public class StudentScoreEntity extends RichEntity implements MyEntity<StudentScoreEntity> {
  private static final long serialVersionUID = 1L;

  /**
   * 主键ID
   */
  @TableId("id")
  private Long id;

  /**
   * 记录创建时间
   */
  @TableField(
      value = "gmt_created",
      insert = "now()"
  )
  private Date gmtCreated;

  /**
   * 记录最后修改时间
   */
  @TableField(
      value = "gmt_modified",
      insert = "now()",
      update = "now()"
  )
  private Date gmtModified;

  /**
   * 逻辑删除标识
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
   * 性别, 0:女; 1:男
   */
  @TableField("gender_man")
  private Integer genderMan;

  /**
   * 学期
   */
  @TableField("school_term")
  private Integer schoolTerm;

  /**
   * 成绩
   */
  @TableField("score")
  private Integer score;

  /**
   * 学号
   */
  @TableField("student_id")
  private Long studentId;

  /**
   * 学科
   */
  @TableField("subject")
  private String subject;

  /**
   * 租户标识
   */
  @TableField("tenant")
  private Long tenant;

  @Override
  public Serializable findPk() {
    return this.id;
  }

  @Override
  public final Class<? extends IEntity> entityClass() {
    return StudentScoreEntity.class;
  }

  /**
   * 实现定义在{@link cn.org.atool.fluent.mybatis.base.IRefs}子类Refs上
   */
  @RefMethod("isDeleted = isDeleted && id = studentId && env = env")
  public StudentEntity findStudent() {
    return super.invoke("findStudent", true);
  }
}
