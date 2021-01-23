package cn.org.atool.fluent.mybatis.generate.mix;

import cn.org.atool.fluent.mybatis.generate.dm.StudentDataMap;
import java.lang.String;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[student]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
public class StudentTableMix implements IMix {
  @Step("清空表[student]数据")
  public StudentTableMix cleanStudentTable() {
    db.table("student").clean();
    return this;
  }

  @Step("准备表[student]数据{1}")
  public StudentTableMix readyStudentTable(StudentDataMap data) {
    db.table("student").insert(data);
    return this;
  }

  @Step("验证表[student]有全表数据{1}")
  public StudentTableMix checkStudentTable(StudentDataMap data, EqMode... modes) {
    db.table("student").query().eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[student]有符合条件{1}的数据{2}")
  public StudentTableMix checkStudentTable(String where, StudentDataMap data, EqMode... modes) {
    db.table("student").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[student]有符合条件{1}的数据{2}")
  public StudentTableMix checkStudentTable(StudentDataMap where, StudentDataMap data,
      EqMode... modes) {
    db.table("student").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[student]有{1}条符合条件{2}的数据")
  public StudentTableMix countStudentTable(int count, StudentDataMap where) {
    db.table("student").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[student]有{1}条符合条件{2}的数据")
  public StudentTableMix countStudentTable(int count, String where) {
    db.table("student").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[student]有{1}条数据")
  public StudentTableMix countStudentTable(int count) {
    db.table("student").query().sizeEq(count);
    return this;
  }
}
