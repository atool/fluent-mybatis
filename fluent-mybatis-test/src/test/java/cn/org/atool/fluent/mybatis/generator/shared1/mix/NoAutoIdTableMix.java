package cn.org.atool.fluent.mybatis.generator.shared1.mix;

import cn.org.atool.fluent.mybatis.generator.shared1.dm.NoAutoIdDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[no_auto_id]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class NoAutoIdTableMix extends BaseMix<NoAutoIdTableMix, NoAutoIdDataMap> implements IMix {
  public NoAutoIdTableMix() {
    super("no_auto_id");
  }

  @Step("清空表[no_auto_id]数据")
  public NoAutoIdTableMix cleanNoAutoIdTable() {
    return super.cleanTable();
  }

  @Step("准备表[no_auto_id]数据{1}")
  public NoAutoIdTableMix readyNoAutoIdTable(NoAutoIdDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[no_auto_id]有全表数据{1}")
  public NoAutoIdTableMix checkNoAutoIdTable(NoAutoIdDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[no_auto_id]有符合条件{1}的数据{2}")
  public NoAutoIdTableMix checkNoAutoIdTable(String where, NoAutoIdDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[no_auto_id]有符合条件{1}的数据{2}")
  public NoAutoIdTableMix checkNoAutoIdTable(NoAutoIdDataMap where, NoAutoIdDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[no_auto_id]有{1}条符合条件{2}的数据")
  public NoAutoIdTableMix countNoAutoIdTable(int count, NoAutoIdDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[no_auto_id]有{1}条符合条件{2}的数据")
  public NoAutoIdTableMix countNoAutoIdTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[no_auto_id]有{1}条数据")
  public NoAutoIdTableMix countNoAutoIdTable(int count) {
    return super.countTable(count);
  }
}
