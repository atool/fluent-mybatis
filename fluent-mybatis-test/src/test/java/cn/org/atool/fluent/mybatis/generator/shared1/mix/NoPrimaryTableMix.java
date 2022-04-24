package cn.org.atool.fluent.mybatis.generator.shared1.mix;

import cn.org.atool.fluent.mybatis.generator.shared1.dm.NoPrimaryDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[no_primary]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class NoPrimaryTableMix extends BaseMix<NoPrimaryTableMix, NoPrimaryDataMap> implements IMix {
  public NoPrimaryTableMix() {
    super("no_primary");
  }

  @Step("清空表[no_primary]数据")
  public NoPrimaryTableMix cleanNoPrimaryTable() {
    return super.cleanTable();
  }

  @Step("准备表[no_primary]数据{1}")
  public NoPrimaryTableMix readyNoPrimaryTable(NoPrimaryDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[no_primary]有全表数据{1}")
  public NoPrimaryTableMix checkNoPrimaryTable(NoPrimaryDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[no_primary]有符合条件{1}的数据{2}")
  public NoPrimaryTableMix checkNoPrimaryTable(String where, NoPrimaryDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[no_primary]有符合条件{1}的数据{2}")
  public NoPrimaryTableMix checkNoPrimaryTable(NoPrimaryDataMap where, NoPrimaryDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[no_primary]有{1}条符合条件{2}的数据")
  public NoPrimaryTableMix countNoPrimaryTable(int count, NoPrimaryDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[no_primary]有{1}条符合条件{2}的数据")
  public NoPrimaryTableMix countNoPrimaryTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[no_primary]有{1}条数据")
  public NoPrimaryTableMix countNoPrimaryTable(int count) {
    return super.countTable(count);
  }
}
