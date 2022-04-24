package cn.org.atool.fluent.mybatis.generator.shared2.mix;

import cn.org.atool.fluent.mybatis.generator.shared2.dm.TeacherDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[teacher]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class TeacherTableMix extends BaseMix<TeacherTableMix, TeacherDataMap> implements IMix {
  public TeacherTableMix() {
    super("teacher");
  }

  @Step("清空表[teacher]数据")
  public TeacherTableMix cleanTeacherTable() {
    return super.cleanTable();
  }

  @Step("准备表[teacher]数据{1}")
  public TeacherTableMix readyTeacherTable(TeacherDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[teacher]有全表数据{1}")
  public TeacherTableMix checkTeacherTable(TeacherDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[teacher]有符合条件{1}的数据{2}")
  public TeacherTableMix checkTeacherTable(String where, TeacherDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[teacher]有符合条件{1}的数据{2}")
  public TeacherTableMix checkTeacherTable(TeacherDataMap where, TeacherDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[teacher]有{1}条符合条件{2}的数据")
  public TeacherTableMix countTeacherTable(int count, TeacherDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[teacher]有{1}条符合条件{2}的数据")
  public TeacherTableMix countTeacherTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[teacher]有{1}条数据")
  public TeacherTableMix countTeacherTable(int count) {
    return super.countTable(count);
  }
}
