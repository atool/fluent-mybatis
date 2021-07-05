package cn.org.atool.fluent.mybatis.generate.mix;

import cn.org.atool.fluent.mybatis.generate.dm.StudentTeacherRelationDataMap;
import java.lang.String;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[student_teacher_relation]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
public class StudentTeacherRelationTableMix implements IMix {
  @Step("清空表[student_teacher_relation]数据")
  public StudentTeacherRelationTableMix cleanStudentTeacherRelationTable() {
    db.table("student_teacher_relation").clean();
    return this;
  }

  @Step("准备表[student_teacher_relation]数据{1}")
  public StudentTeacherRelationTableMix readyStudentTeacherRelationTable(
      StudentTeacherRelationDataMap data) {
    db.table("student_teacher_relation").insert(data);
    return this;
  }

  @Step("验证表[student_teacher_relation]有全表数据{1}")
  public StudentTeacherRelationTableMix checkStudentTeacherRelationTable(
      StudentTeacherRelationDataMap data, EqMode... modes) {
    db.table("student_teacher_relation").query().eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[student_teacher_relation]有符合条件{1}的数据{2}")
  public StudentTeacherRelationTableMix checkStudentTeacherRelationTable(String where,
      StudentTeacherRelationDataMap data, EqMode... modes) {
    db.table("student_teacher_relation").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[student_teacher_relation]有符合条件{1}的数据{2}")
  public StudentTeacherRelationTableMix checkStudentTeacherRelationTable(
      StudentTeacherRelationDataMap where, StudentTeacherRelationDataMap data, EqMode... modes) {
    db.table("student_teacher_relation").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[student_teacher_relation]有{1}条符合条件{2}的数据")
  public StudentTeacherRelationTableMix countStudentTeacherRelationTable(int count,
      StudentTeacherRelationDataMap where) {
    db.table("student_teacher_relation").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[student_teacher_relation]有{1}条符合条件{2}的数据")
  public StudentTeacherRelationTableMix countStudentTeacherRelationTable(int count, String where) {
    db.table("student_teacher_relation").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[student_teacher_relation]有{1}条数据")
  public StudentTeacherRelationTableMix countStudentTeacherRelationTable(int count) {
    db.table("student_teacher_relation").query().sizeEq(count);
    return this;
  }
}
