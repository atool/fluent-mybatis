package cn.org.atool.fluent.mybatis.generator.shared5.mix;

import cn.org.atool.fluent.mybatis.generator.shared5.dm.IdcardDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[idcard]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class IdcardTableMix extends BaseMix<IdcardTableMix, IdcardDataMap> implements IMix {
  public IdcardTableMix() {
    super("idcard");
  }

  @Step("清空表[idcard]数据")
  public IdcardTableMix cleanIdcardTable() {
    return super.cleanTable();
  }

  @Step("准备表[idcard]数据{1}")
  public IdcardTableMix readyIdcardTable(IdcardDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[idcard]有全表数据{1}")
  public IdcardTableMix checkIdcardTable(IdcardDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[idcard]有符合条件{1}的数据{2}")
  public IdcardTableMix checkIdcardTable(String where, IdcardDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[idcard]有符合条件{1}的数据{2}")
  public IdcardTableMix checkIdcardTable(IdcardDataMap where, IdcardDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[idcard]有{1}条符合条件{2}的数据")
  public IdcardTableMix countIdcardTable(int count, IdcardDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[idcard]有{1}条符合条件{2}的数据")
  public IdcardTableMix countIdcardTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[idcard]有{1}条数据")
  public IdcardTableMix countIdcardTable(int count) {
    return super.countTable(count);
  }
}
