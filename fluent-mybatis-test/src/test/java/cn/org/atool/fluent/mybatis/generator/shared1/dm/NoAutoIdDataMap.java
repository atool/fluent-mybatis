package cn.org.atool.fluent.mybatis.generator.shared1.dm;

import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * NoAutoIdDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("no_auto_id")
@SuppressWarnings({"unused"})
public class NoAutoIdDataMap extends TableDataMap<NoAutoIdDataMap> {
  @ColumnDef(
      value = "id",
      type = "VARCHAR(50)",
      primary = true,
      notNull = true
  )
  public final transient KeyValue<NoAutoIdDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "column_1",
      type = "VARCHAR(20)"
  )
  public final transient KeyValue<NoAutoIdDataMap> column1 = new KeyValue<>(this, "column_1", "column1", supplier);

  @ColumnDef(
      value = "lock_version",
      type = "BIGINT",
      notNull = true,
      defaultValue = "0"
  )
  public final transient KeyValue<NoAutoIdDataMap> lockVersion = new KeyValue<>(this, "lock_version", "lockVersion", supplier);

  NoAutoIdDataMap(boolean isTable) {
    super("no_auto_id", isTable);
  }

  NoAutoIdDataMap(boolean isTable, int size) {
    super("no_auto_id", isTable, size);
  }

  /**
   * 创建NoAutoIdDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public NoAutoIdDataMap init() {
    return this;
  }

  public static NoAutoIdDataMap table() {
    return new NoAutoIdDataMap(true, 1);
  }

  public static NoAutoIdDataMap table(int size) {
    return new NoAutoIdDataMap(true, size);
  }

  public static NoAutoIdDataMap entity() {
    return new NoAutoIdDataMap(false, 1);
  }

  public static NoAutoIdDataMap entity(int size) {
    return new NoAutoIdDataMap(false, size);
  }

  public static class Factory extends BaseFactory<NoAutoIdDataMap> {
    public Factory() {
      super(NoAutoIdDataMap.class);
    }
  }
}
