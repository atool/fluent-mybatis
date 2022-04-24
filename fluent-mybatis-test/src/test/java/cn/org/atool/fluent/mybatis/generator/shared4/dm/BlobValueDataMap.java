package cn.org.atool.fluent.mybatis.generator.shared4.dm;

import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * BlobValueDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("blob_value")
@SuppressWarnings({"unused"})
public class BlobValueDataMap extends TableDataMap<BlobValueDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT UNSIGNED",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<BlobValueDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "blob_value",
      type = "BLOB"
  )
  public final transient KeyValue<BlobValueDataMap> blobValue = new KeyValue<>(this, "blob_value", "blobValue", supplier);

  @ColumnDef(
      value = "max",
      type = "BIGINT"
  )
  public final transient KeyValue<BlobValueDataMap> max = new KeyValue<>(this, "max", "max", supplier);

  @ColumnDef(
      value = "min",
      type = "BIGINT"
  )
  public final transient KeyValue<BlobValueDataMap> min = new KeyValue<>(this, "min", "min", supplier);

  @ColumnDef(
      value = "origin",
      type = "VARCHAR(20)"
  )
  public final transient KeyValue<BlobValueDataMap> origin = new KeyValue<>(this, "origin", "origin", supplier);

  BlobValueDataMap(boolean isTable) {
    super("blob_value", isTable);
  }

  BlobValueDataMap(boolean isTable, int size) {
    super("blob_value", isTable, size);
  }

  /**
   * 创建BlobValueDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public BlobValueDataMap init() {
    this.id.autoIncrease();
    return this;
  }

  public static BlobValueDataMap table() {
    return new BlobValueDataMap(true, 1);
  }

  public static BlobValueDataMap table(int size) {
    return new BlobValueDataMap(true, size);
  }

  public static BlobValueDataMap entity() {
    return new BlobValueDataMap(false, 1);
  }

  public static BlobValueDataMap entity(int size) {
    return new BlobValueDataMap(false, size);
  }

  public static class Factory extends BaseFactory<BlobValueDataMap> {
    public Factory() {
      super(BlobValueDataMap.class);
    }
  }
}
