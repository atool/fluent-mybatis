package cn.org.atool.fluent.mybatis.generate.mix;

import cn.org.atool.fluent.mybatis.generate.dm.AddressDataMap;
import java.lang.String;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[address]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
public class AddressTableMix implements IMix {
  @Step("清空表[address]数据")
  public AddressTableMix cleanAddressTable() {
    db.table("address").clean();
    return this;
  }

  @Step("准备表[address]数据{1}")
  public AddressTableMix readyAddressTable(AddressDataMap data) {
    db.table("address").insert(data);
    return this;
  }

  @Step("验证表[address]有全表数据{1}")
  public AddressTableMix checkAddressTable(AddressDataMap data) {
    db.table("address").query().eqDataMap(data, EqMode.IGNORE_ORDER);
    return this;
  }

  @Step("验证表[address]有符合条件{1}的数据{2}")
  public AddressTableMix checkAddressTable(String where, AddressDataMap data) {
    db.table("address").queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
    return this;
  }

  @Step("验证表[address]有符合条件{1}的数据{2}")
  public AddressTableMix checkAddressTable(AddressDataMap where, AddressDataMap data) {
    db.table("address").queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
    return this;
  }

  @Step("验证表[address]有{1}条符合条件{2}的数据")
  public AddressTableMix countAddressTable(int count, AddressDataMap where) {
    db.table("address").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[address]有{1}条符合条件{2}的数据")
  public AddressTableMix countAddressTable(int count, String where) {
    db.table("address").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[address]有{1}条数据")
  public AddressTableMix countAddressTable(int count) {
    db.table("address").query().sizeEq(count);
    return this;
  }
}
