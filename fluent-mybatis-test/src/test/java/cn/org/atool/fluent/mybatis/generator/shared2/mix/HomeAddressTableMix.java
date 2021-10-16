package cn.org.atool.fluent.mybatis.generator.shared2.mix;

import cn.org.atool.fluent.mybatis.generator.shared2.dm.HomeAddressDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[home_address]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class HomeAddressTableMix implements IMix {
  @Step("清空表[home_address]数据")
  public HomeAddressTableMix cleanHomeAddressTable() {
    db.table("home_address").clean();
    return this;
  }

  @Step("准备表[home_address]数据{1}")
  public HomeAddressTableMix readyHomeAddressTable(HomeAddressDataMap data) {
    db.table("home_address").insert(data);
    return this;
  }

  @Step("验证表[home_address]有全表数据{1}")
  public HomeAddressTableMix checkHomeAddressTable(HomeAddressDataMap data, EqMode... modes) {
    db.table("home_address").query().eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[home_address]有符合条件{1}的数据{2}")
  public HomeAddressTableMix checkHomeAddressTable(String where, HomeAddressDataMap data,
      EqMode... modes) {
    db.table("home_address").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[home_address]有符合条件{1}的数据{2}")
  public HomeAddressTableMix checkHomeAddressTable(HomeAddressDataMap where,
      HomeAddressDataMap data, EqMode... modes) {
    db.table("home_address").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[home_address]有{1}条符合条件{2}的数据")
  public HomeAddressTableMix countHomeAddressTable(int count, HomeAddressDataMap where) {
    db.table("home_address").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[home_address]有{1}条符合条件{2}的数据")
  public HomeAddressTableMix countHomeAddressTable(int count, String where) {
    db.table("home_address").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[home_address]有{1}条数据")
  public HomeAddressTableMix countHomeAddressTable(int count) {
    db.table("home_address").query().sizeEq(count);
    return this;
  }
}
