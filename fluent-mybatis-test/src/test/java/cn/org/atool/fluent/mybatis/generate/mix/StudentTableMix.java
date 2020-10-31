package cn.org.atool.fluent.mybatis.generate.mix;

import cn.org.atool.fluent.mybatis.generate.dm.StudentDataMap;
import java.lang.String;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[t_student]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
public class StudentTableMix implements IMix {
  @Step("清空表[t_student]数据")
  public StudentTableMix cleanStudentTable() {
    db.table("t_student").clean();
    return this;
  }

  @Step("准备表[t_student]数据{1}")
  public StudentTableMix readyStudentTable(StudentDataMap data) {
    db.table("t_student").insert(data);
    return this;
  }

  @Step("验证表[t_student]有全表数据{1}")
  public StudentTableMix checkStudentTable(StudentDataMap data) {
    db.table("t_student").query().eqDataMap(data, EqMode.IGNORE_ORDER);
    return this;
  }

  @Step("验证表[t_student]有符合条件{1}的数据{2}")
  public StudentTableMix checkStudentTable(String where, StudentDataMap data) {
    db.table("t_student").queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
    return this;
  }

  @Step("验证表[t_student]有符合条件{1}的数据{2}")
  public StudentTableMix checkStudentTable(StudentDataMap where, StudentDataMap data) {
    db.table("t_student").queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
    return this;
  }

  @Step("验证表[t_student]有{1}条符合条件{2}的数据")
  public StudentTableMix countStudentTable(int count, StudentDataMap where) {
    db.table("t_student").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[t_student]有{1}条符合条件{2}的数据")
  public StudentTableMix countStudentTable(int count, String where) {
    db.table("t_student").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[t_student]有{1}条数据")
  public StudentTableMix countStudentTable(int count) {
    db.table("t_student").query().sizeEq(count);
    return this;
  }
}
