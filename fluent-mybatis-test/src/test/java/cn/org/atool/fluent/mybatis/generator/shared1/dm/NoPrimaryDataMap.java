package cn.org.atool.fluent.mybatis.generator.shared1.dm;

import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * NoPrimaryDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("no_primary")
@SuppressWarnings({"unused"})
public class NoPrimaryDataMap extends TableDataMap<NoPrimaryDataMap> {
  @ColumnDef(
      value = "column_1",
      type = "INT"
  )
  public final transient KeyValue<NoPrimaryDataMap> column1 = new KeyValue<>(this, "column_1", "column1", supplier);

  @ColumnDef(
      value = "column_2",
      type = "VARCHAR(100)"
  )
  public final transient KeyValue<NoPrimaryDataMap> column2 = new KeyValue<>(this, "column_2", "column2", supplier);

  @ColumnDef(
      value = "alias",
      type = "VARCHAR(20)"
  )
  public final transient KeyValue<NoPrimaryDataMap> alias = new KeyValue<>(this, "alias", "alias", supplier);

  NoPrimaryDataMap(boolean isTable) {
    super("no_primary", isTable);
  }

  NoPrimaryDataMap(boolean isTable, int size) {
    super("no_primary", isTable, size);
  }

  /**
   * 创建NoPrimaryDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public NoPrimaryDataMap init() {
    return this;
  }

  public static NoPrimaryDataMap table() {
    return new NoPrimaryDataMap(true, 1);
  }

  public static NoPrimaryDataMap table(int size) {
    return new NoPrimaryDataMap(true, size);
  }

  public static NoPrimaryDataMap entity() {
    return new NoPrimaryDataMap(false, 1);
  }

  public static NoPrimaryDataMap entity(int size) {
    return new NoPrimaryDataMap(false, size);
  }

  public static class Factory extends BaseFactory<NoPrimaryDataMap> {
    public Factory() {
      super(NoPrimaryDataMap.class);
    }
  }
}
