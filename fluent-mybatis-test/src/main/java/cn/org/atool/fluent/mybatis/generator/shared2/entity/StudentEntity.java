package cn.org.atool.fluent.mybatis.generator.shared2.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.RefMethod;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.customize.MyEntity;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * StudentEntity: 数据映射实体定义
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
    table = "student",
    schema = "fluent_mybatis",
    mapperBeanPrefix = "my",
    defaults = MyCustomerInterface.class
)
public class StudentEntity extends MyEntity<StudentEntity> {
  private static final long serialVersionUID = 1L;

  @TableField(
      value = "address",
      desc = "家庭详细住址"
  )
  private String address;

  @TableField(
      value = "age",
      desc = "年龄"
  )
  private Integer age;

  @TableField(
      value = "birthday",
      desc = "生日"
  )
  private Date birthday;

  @TableField(
      value = "bonus_points",
      desc = "积分"
  )
  private Long bonusPoints;

  @TableField(
      value = "desk_mate_id",
      desc = "同桌"
  )
  private Long deskMateId;

  @TableField(
      value = "email",
      desc = "邮箱"
  )
  private String email;

  @TableField(
      value = "gender",
      desc = "性别, 0:女; 1:男"
  )
  private Integer gender;

  @TableField(
      value = "grade",
      desc = "年级"
  )
  private Integer grade;

  @TableField(
      value = "home_address_id",
      desc = "home_address外键"
  )
  private Long homeAddressId;

  @TableField(
      value = "home_county_id",
      desc = "家庭所在区县"
  )
  private Long homeCountyId;

  @TableField(
      value = "phone",
      desc = "电话"
  )
  private String phone;

  @TableField(
      value = "status",
      desc = "状态(字典)"
  )
  private String status;

  @TableField(
      value = "user_name",
      desc = "名字"
  )
  private String userName;

  @TableField(
      value = "version",
      notLarge = false,
      desc = "版本号"
  )
  private String version;

  @Override
  public final Class entityClass() {
    return StudentEntity.class;
  }

  /**
   * @see cn.org.atool.fluent.mybatis.generator.shared2.IEntityRelation#findDeskMateOfStudentEntity(List)
   */
  @RefMethod("deskMateId = id")
  public StudentEntity findDeskMate() {
    return super.invoke("findDeskMate", true);
  }

  /**
   * @see cn.org.atool.fluent.mybatis.generator.shared2.IEntityRelation#findStudentScoreListOfStudentEntity(List)
   */
  @RefMethod("studentId = id && isDeleted = isDeleted && env = env")
  public List<StudentScoreEntity> findStudentScoreList() {
    return super.invoke("findStudentScoreList", true);
  }

  /**
   * @see cn.org.atool.fluent.mybatis.generator.shared2.IEntityRelation#findEnglishScoreOfStudentEntity(StudentEntity)
   */
  @RefMethod
  public StudentScoreEntity findEnglishScore() {
    return super.invoke("findEnglishScore", true);
  }

  /**
   * @see cn.org.atool.fluent.mybatis.generator.shared2.IEntityRelation#findTeacherListOfStudentEntity(StudentEntity)
   */
  @RefMethod
  public List<TeacherEntity> findTeacherList() {
    return super.invoke("findTeacherList", true);
  }
}
