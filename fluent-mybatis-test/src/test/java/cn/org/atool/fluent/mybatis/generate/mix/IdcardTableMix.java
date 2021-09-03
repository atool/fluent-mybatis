package cn.org.atool.fluent.mybatis.generate.mix;

import cn.org.atool.fluent.mybatis.generate.dm.IdcardDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[idcard]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
public class IdcardTableMix implements IMix {
  @Step("清空表[idcard]数据")
  public IdcardTableMix cleanIdcardTable() {
    db.table("idcard").clean();
    return this;
  }

  @Step("准备表[idcard]数据{1}")
  public IdcardTableMix readyIdcardTable(IdcardDataMap data) {
    db.table("idcard").insert(data);
    return this;
  }

  @Step("验证表[idcard]有全表数据{1}")
  public IdcardTableMix checkIdcardTable(IdcardDataMap data, EqMode... modes) {
    db.table("idcard").query().eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[idcard]有符合条件{1}的数据{2}")
  public IdcardTableMix checkIdcardTable(String where, IdcardDataMap data, EqMode... modes) {
    db.table("idcard").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[idcard]有符合条件{1}的数据{2}")
  public IdcardTableMix checkIdcardTable(IdcardDataMap where, IdcardDataMap data, EqMode... modes) {
    db.table("idcard").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[idcard]有{1}条符合条件{2}的数据")
  public IdcardTableMix countIdcardTable(int count, IdcardDataMap where) {
    db.table("idcard").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[idcard]有{1}条符合条件{2}的数据")
  public IdcardTableMix countIdcardTable(int count, String where) {
    db.table("idcard").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[idcard]有{1}条数据")
  public IdcardTableMix countIdcardTable(int count) {
    db.table("idcard").query().sizeEq(count);
    return this;
  }
}
