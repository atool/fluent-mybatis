package cn.org.atool.fluent.mybatis.generator.shared2.mix;

import cn.org.atool.fluent.mybatis.generator.shared2.dm.StudentDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[student]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class StudentTableMix extends BaseMix<StudentTableMix, StudentDataMap> implements IMix {
  public StudentTableMix() {
    super("student");
  }

  @Step("清空表[student]数据")
  public StudentTableMix cleanStudentTable() {
    return super.cleanTable();
  }

  @Step("准备表[student]数据{1}")
  public StudentTableMix readyStudentTable(StudentDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[student]有全表数据{1}")
  public StudentTableMix checkStudentTable(StudentDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[student]有符合条件{1}的数据{2}")
  public StudentTableMix checkStudentTable(String where, StudentDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[student]有符合条件{1}的数据{2}")
  public StudentTableMix checkStudentTable(StudentDataMap where, StudentDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[student]有{1}条符合条件{2}的数据")
  public StudentTableMix countStudentTable(int count, StudentDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[student]有{1}条符合条件{2}的数据")
  public StudentTableMix countStudentTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[student]有{1}条数据")
  public StudentTableMix countStudentTable(int count) {
    return super.countTable(count);
  }
}
