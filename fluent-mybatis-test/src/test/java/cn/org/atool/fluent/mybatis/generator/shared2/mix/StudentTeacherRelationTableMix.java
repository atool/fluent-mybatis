package cn.org.atool.fluent.mybatis.generator.shared2.mix;

import cn.org.atool.fluent.mybatis.generator.shared2.dm.StudentTeacherRelationDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[student_teacher_relation]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class StudentTeacherRelationTableMix extends BaseMix<StudentTeacherRelationTableMix, StudentTeacherRelationDataMap> implements IMix {
  public StudentTeacherRelationTableMix() {
    super("student_teacher_relation");
  }

  @Step("清空表[student_teacher_relation]数据")
  public StudentTeacherRelationTableMix cleanStudentTeacherRelationTable() {
    return super.cleanTable();
  }

  @Step("准备表[student_teacher_relation]数据{1}")
  public StudentTeacherRelationTableMix readyStudentTeacherRelationTable(
      StudentTeacherRelationDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[student_teacher_relation]有全表数据{1}")
  public StudentTeacherRelationTableMix checkStudentTeacherRelationTable(
      StudentTeacherRelationDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[student_teacher_relation]有符合条件{1}的数据{2}")
  public StudentTeacherRelationTableMix checkStudentTeacherRelationTable(String where,
      StudentTeacherRelationDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[student_teacher_relation]有符合条件{1}的数据{2}")
  public StudentTeacherRelationTableMix checkStudentTeacherRelationTable(
      StudentTeacherRelationDataMap where, StudentTeacherRelationDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[student_teacher_relation]有{1}条符合条件{2}的数据")
  public StudentTeacherRelationTableMix countStudentTeacherRelationTable(int count,
      StudentTeacherRelationDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[student_teacher_relation]有{1}条符合条件{2}的数据")
  public StudentTeacherRelationTableMix countStudentTeacherRelationTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[student_teacher_relation]有{1}条数据")
  public StudentTeacherRelationTableMix countStudentTeacherRelationTable(int count) {
    return super.countTable(count);
  }
}
