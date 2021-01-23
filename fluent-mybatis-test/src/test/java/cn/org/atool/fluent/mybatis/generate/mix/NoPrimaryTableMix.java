package cn.org.atool.fluent.mybatis.generate.mix;

import cn.org.atool.fluent.mybatis.generate.dm.NoPrimaryDataMap;
import java.lang.String;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[no_primary]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
public class NoPrimaryTableMix implements IMix {
  @Step("清空表[no_primary]数据")
  public NoPrimaryTableMix cleanNoPrimaryTable() {
    db.table("no_primary").clean();
    return this;
  }

  @Step("准备表[no_primary]数据{1}")
  public NoPrimaryTableMix readyNoPrimaryTable(NoPrimaryDataMap data) {
    db.table("no_primary").insert(data);
    return this;
  }

  @Step("验证表[no_primary]有全表数据{1}")
  public NoPrimaryTableMix checkNoPrimaryTable(NoPrimaryDataMap data, EqMode... modes) {
    db.table("no_primary").query().eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[no_primary]有符合条件{1}的数据{2}")
  public NoPrimaryTableMix checkNoPrimaryTable(String where, NoPrimaryDataMap data,
      EqMode... modes) {
    db.table("no_primary").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[no_primary]有符合条件{1}的数据{2}")
  public NoPrimaryTableMix checkNoPrimaryTable(NoPrimaryDataMap where, NoPrimaryDataMap data,
      EqMode... modes) {
    db.table("no_primary").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[no_primary]有{1}条符合条件{2}的数据")
  public NoPrimaryTableMix countNoPrimaryTable(int count, NoPrimaryDataMap where) {
    db.table("no_primary").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[no_primary]有{1}条符合条件{2}的数据")
  public NoPrimaryTableMix countNoPrimaryTable(int count, String where) {
    db.table("no_primary").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[no_primary]有{1}条数据")
  public NoPrimaryTableMix countNoPrimaryTable(int count) {
    db.table("no_primary").query().sizeEq(count);
    return this;
  }
}
