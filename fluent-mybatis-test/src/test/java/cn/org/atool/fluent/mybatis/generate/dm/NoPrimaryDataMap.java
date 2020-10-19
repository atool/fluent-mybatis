package cn.org.atool.fluent.mybatis.generate.dm;

import java.lang.Boolean;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.test4j.module.ICore.DataMap;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.tools.datagen.KeyValue;

/**
 * NoPrimaryDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("no_primary")
public class NoPrimaryDataMap extends DataMap<NoPrimaryDataMap> {
  private boolean isTable;

  private Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "column_1",
      type = "int(11)"
  )
  public final transient KeyValue<NoPrimaryDataMap> column1 = new KeyValue(this, "column_1", "column1", supplier);

  @ColumnDef(
      value = "column_2",
      type = "varchar(100)"
  )
  public final transient KeyValue<NoPrimaryDataMap> column2 = new KeyValue(this, "column_2", "column2", supplier);

  NoPrimaryDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  NoPrimaryDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建NoPrimaryDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public NoPrimaryDataMap init() {
    return this;
  }

  public NoPrimaryDataMap with(Consumer<NoPrimaryDataMap> init) {
    init.accept(this);
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

  public static class Factory {
    public NoPrimaryDataMap table() {
      return NoPrimaryDataMap.table();
    }

    public NoPrimaryDataMap table(int size) {
      return  NoPrimaryDataMap.table(size);
    }

    public NoPrimaryDataMap initTable() {
      return NoPrimaryDataMap.table().init();
    }

    public NoPrimaryDataMap initTable(int size) {
      return  NoPrimaryDataMap.table(size).init();
    }

    public NoPrimaryDataMap entity() {
      return NoPrimaryDataMap.entity();
    }

    public NoPrimaryDataMap entity(int size) {
      return  NoPrimaryDataMap.entity(size);
    }
  }
}
