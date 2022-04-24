package cn.org.atool.fluent.mybatis.generator.shared2.mix;

import cn.org.atool.fluent.mybatis.generator.shared2.dm.HomeAddressDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[home_address]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class HomeAddressTableMix extends BaseMix<HomeAddressTableMix, HomeAddressDataMap> implements IMix {
  public HomeAddressTableMix() {
    super("home_address");
  }

  @Step("清空表[home_address]数据")
  public HomeAddressTableMix cleanHomeAddressTable() {
    return super.cleanTable();
  }

  @Step("准备表[home_address]数据{1}")
  public HomeAddressTableMix readyHomeAddressTable(HomeAddressDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[home_address]有全表数据{1}")
  public HomeAddressTableMix checkHomeAddressTable(HomeAddressDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[home_address]有符合条件{1}的数据{2}")
  public HomeAddressTableMix checkHomeAddressTable(String where, HomeAddressDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[home_address]有符合条件{1}的数据{2}")
  public HomeAddressTableMix checkHomeAddressTable(HomeAddressDataMap where,
      HomeAddressDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[home_address]有{1}条符合条件{2}的数据")
  public HomeAddressTableMix countHomeAddressTable(int count, HomeAddressDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[home_address]有{1}条符合条件{2}的数据")
  public HomeAddressTableMix countHomeAddressTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[home_address]有{1}条数据")
  public HomeAddressTableMix countHomeAddressTable(int count) {
    return super.countTable(count);
  }
}
