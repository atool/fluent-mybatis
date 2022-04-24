package cn.org.atool.fluent.mybatis.generator.shared4.mix;

import cn.org.atool.fluent.mybatis.generator.shared4.dm.MyEnumTypeDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[my_enum_type]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class MyEnumTypeTableMix extends BaseMix<MyEnumTypeTableMix, MyEnumTypeDataMap> implements IMix {
  public MyEnumTypeTableMix() {
    super("my_enum_type");
  }

  @Step("清空表[my_enum_type]数据")
  public MyEnumTypeTableMix cleanMyEnumTypeTable() {
    return super.cleanTable();
  }

  @Step("准备表[my_enum_type]数据{1}")
  public MyEnumTypeTableMix readyMyEnumTypeTable(MyEnumTypeDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[my_enum_type]有全表数据{1}")
  public MyEnumTypeTableMix checkMyEnumTypeTable(MyEnumTypeDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[my_enum_type]有符合条件{1}的数据{2}")
  public MyEnumTypeTableMix checkMyEnumTypeTable(String where, MyEnumTypeDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[my_enum_type]有符合条件{1}的数据{2}")
  public MyEnumTypeTableMix checkMyEnumTypeTable(MyEnumTypeDataMap where, MyEnumTypeDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[my_enum_type]有{1}条符合条件{2}的数据")
  public MyEnumTypeTableMix countMyEnumTypeTable(int count, MyEnumTypeDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[my_enum_type]有{1}条符合条件{2}的数据")
  public MyEnumTypeTableMix countMyEnumTypeTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[my_enum_type]有{1}条数据")
  public MyEnumTypeTableMix countMyEnumTypeTable(int count) {
    return super.countTable(count);
  }
}
