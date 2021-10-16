package cn.org.atool.fluent.mybatis.generator.shared4.mix;

import cn.org.atool.fluent.mybatis.generator.shared4.dm.MyEnumTypeDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[my_enum_type]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class MyEnumTypeTableMix implements IMix {
  @Step("清空表[my_enum_type]数据")
  public MyEnumTypeTableMix cleanMyEnumTypeTable() {
    db.table("my_enum_type").clean();
    return this;
  }

  @Step("准备表[my_enum_type]数据{1}")
  public MyEnumTypeTableMix readyMyEnumTypeTable(MyEnumTypeDataMap data) {
    db.table("my_enum_type").insert(data);
    return this;
  }

  @Step("验证表[my_enum_type]有全表数据{1}")
  public MyEnumTypeTableMix checkMyEnumTypeTable(MyEnumTypeDataMap data, EqMode... modes) {
    db.table("my_enum_type").query().eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[my_enum_type]有符合条件{1}的数据{2}")
  public MyEnumTypeTableMix checkMyEnumTypeTable(String where, MyEnumTypeDataMap data,
      EqMode... modes) {
    db.table("my_enum_type").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[my_enum_type]有符合条件{1}的数据{2}")
  public MyEnumTypeTableMix checkMyEnumTypeTable(MyEnumTypeDataMap where, MyEnumTypeDataMap data,
      EqMode... modes) {
    db.table("my_enum_type").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[my_enum_type]有{1}条符合条件{2}的数据")
  public MyEnumTypeTableMix countMyEnumTypeTable(int count, MyEnumTypeDataMap where) {
    db.table("my_enum_type").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[my_enum_type]有{1}条符合条件{2}的数据")
  public MyEnumTypeTableMix countMyEnumTypeTable(int count, String where) {
    db.table("my_enum_type").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[my_enum_type]有{1}条数据")
  public MyEnumTypeTableMix countMyEnumTypeTable(int count) {
    db.table("my_enum_type").query().sizeEq(count);
    return this;
  }
}
