package cn.org.atool.fluent.mybatis.generate.mix;

import cn.org.atool.fluent.mybatis.generate.dm.TeacherDataMap;
import java.lang.String;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[teacher]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
public class TeacherTableMix implements IMix {
  @Step("清空表[teacher]数据")
  public TeacherTableMix cleanTeacherTable() {
    db.table("teacher").clean();
    return this;
  }

  @Step("准备表[teacher]数据{1}")
  public TeacherTableMix readyTeacherTable(TeacherDataMap data) {
    db.table("teacher").insert(data);
    return this;
  }

  @Step("验证表[teacher]有全表数据{1}")
  public TeacherTableMix checkTeacherTable(TeacherDataMap data, EqMode... modes) {
    db.table("teacher").query().eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[teacher]有符合条件{1}的数据{2}")
  public TeacherTableMix checkTeacherTable(String where, TeacherDataMap data, EqMode... modes) {
    db.table("teacher").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[teacher]有符合条件{1}的数据{2}")
  public TeacherTableMix checkTeacherTable(TeacherDataMap where, TeacherDataMap data,
      EqMode... modes) {
    db.table("teacher").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[teacher]有{1}条符合条件{2}的数据")
  public TeacherTableMix countTeacherTable(int count, TeacherDataMap where) {
    db.table("teacher").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[teacher]有{1}条符合条件{2}的数据")
  public TeacherTableMix countTeacherTable(int count, String where) {
    db.table("teacher").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[teacher]有{1}条数据")
  public TeacherTableMix countTeacherTable(int count) {
    db.table("teacher").query().sizeEq(count);
    return this;
  }
}
