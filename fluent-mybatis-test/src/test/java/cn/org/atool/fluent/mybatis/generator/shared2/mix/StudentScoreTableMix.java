package cn.org.atool.fluent.mybatis.generator.shared2.mix;

import cn.org.atool.fluent.mybatis.generator.shared2.dm.StudentScoreDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[student_score]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class StudentScoreTableMix extends BaseMix<StudentScoreTableMix, StudentScoreDataMap> implements IMix {
  public StudentScoreTableMix() {
    super("student_score");
  }

  @Step("清空表[student_score]数据")
  public StudentScoreTableMix cleanStudentScoreTable() {
    return super.cleanTable();
  }

  @Step("准备表[student_score]数据{1}")
  public StudentScoreTableMix readyStudentScoreTable(StudentScoreDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[student_score]有全表数据{1}")
  public StudentScoreTableMix checkStudentScoreTable(StudentScoreDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[student_score]有符合条件{1}的数据{2}")
  public StudentScoreTableMix checkStudentScoreTable(String where, StudentScoreDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[student_score]有符合条件{1}的数据{2}")
  public StudentScoreTableMix checkStudentScoreTable(StudentScoreDataMap where,
      StudentScoreDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[student_score]有{1}条符合条件{2}的数据")
  public StudentScoreTableMix countStudentScoreTable(int count, StudentScoreDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[student_score]有{1}条符合条件{2}的数据")
  public StudentScoreTableMix countStudentScoreTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[student_score]有{1}条数据")
  public StudentScoreTableMix countStudentScoreTable(int count) {
    return super.countTable(count);
  }
}
