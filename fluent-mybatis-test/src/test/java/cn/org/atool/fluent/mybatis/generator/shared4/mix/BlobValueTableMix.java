package cn.org.atool.fluent.mybatis.generator.shared4.mix;

import cn.org.atool.fluent.mybatis.generator.shared4.dm.BlobValueDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[blob_value]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class BlobValueTableMix extends BaseMix<BlobValueTableMix, BlobValueDataMap> implements IMix {
  public BlobValueTableMix() {
    super("blob_value");
  }

  @Step("清空表[blob_value]数据")
  public BlobValueTableMix cleanBlobValueTable() {
    return super.cleanTable();
  }

  @Step("准备表[blob_value]数据{1}")
  public BlobValueTableMix readyBlobValueTable(BlobValueDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[blob_value]有全表数据{1}")
  public BlobValueTableMix checkBlobValueTable(BlobValueDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[blob_value]有符合条件{1}的数据{2}")
  public BlobValueTableMix checkBlobValueTable(String where, BlobValueDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[blob_value]有符合条件{1}的数据{2}")
  public BlobValueTableMix checkBlobValueTable(BlobValueDataMap where, BlobValueDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[blob_value]有{1}条符合条件{2}的数据")
  public BlobValueTableMix countBlobValueTable(int count, BlobValueDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[blob_value]有{1}条符合条件{2}的数据")
  public BlobValueTableMix countBlobValueTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[blob_value]有{1}条数据")
  public BlobValueTableMix countBlobValueTable(int count) {
    return super.countTable(count);
  }
}
