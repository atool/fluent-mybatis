package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.RefMethod;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.customize.MyEntity;
import java.io.Serializable;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * TeacherEntity: 数据映射实体定义
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
    table = "teacher",
    mapperBeanPrefix = "my",
    defaults = MyCustomerInterface.class
)
public class TeacherEntity extends RichEntity implements MyEntity<TeacherEntity> {
  private static final long serialVersionUID = 1L;

  /**
   * 主键id
   */
  @TableId("id")
  private Long id;

  /**
   * 名字
   */
  @TableField("user_name")
  private String userName;

  @Override
  public Serializable findPk() {
    return this.id;
  }

  @Override
  public final Class<? extends IEntity> entityClass() {
    return TeacherEntity.class;
  }

  /**
   * 实现定义在{@link cn.org.atool.fluent.mybatis.base.IRefs}子类Refs上
   */
  @RefMethod
  public List<StudentEntity> findStudentList() {
    return super.invoke("findStudentList", true);
  }
}
