package cn.org.atool.fluent.mybatis.generator.shared1.dm;

import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.IDatabase;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.tools.datagen.DataMap;
import org.test4j.tools.datagen.IDataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * NoPrimaryDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("no_primary")
@SuppressWarnings({"unused", "rawtypes"})
public class NoPrimaryDataMap extends DataMap<NoPrimaryDataMap> {
  private boolean isTable;

  private final Supplier<Boolean> supplier = () -> this.isTable;

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

  /**
   * DataMap数据和表[no_primary]数据比较
   */
  public NoPrimaryDataMap eqTable(EqMode... modes) {
    IDatabase.db.table("no_primary").query().eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[no_primary]数据比较
   */
  public NoPrimaryDataMap eqQuery(String query, EqMode... modes) {
    IDatabase.db.table("no_primary").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[no_primary]数据比较
   */
  public NoPrimaryDataMap eqQuery(IDataMap query, EqMode... modes) {
    IDatabase.db.table("no_primary").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * 清空[no_primary]表数据
   */
  public NoPrimaryDataMap clean() {
    IDatabase.db.cleanTable("no_primary");
    return this;
  }

  /**
   * 插入[no_primary]表数据
   */
  public NoPrimaryDataMap insert() {
    IDatabase.db.table("no_primary").insert(this);
    return this;
  }

  /**
   * 先清空, 再插入[no_primary]表数据
   */
  public NoPrimaryDataMap cleanAndInsert() {
    return this.clean().insert();
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
