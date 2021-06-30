package cn.org.atool.fluent.mybatis.generate.dm;

import java.lang.Boolean;
import java.lang.String;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.IDatabase;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.tools.datagen.DataMap;
import org.test4j.tools.datagen.IDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * BlobValueDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("blob_value")
public class BlobValueDataMap extends DataMap<BlobValueDataMap> {
  private boolean isTable;

  private Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "id",
      type = "bigint(21) unsigned",
      primary = true,
      autoIncrease = true
  )
  public final transient KeyValue<BlobValueDataMap> id = new KeyValue(this, "id", "id", supplier);

  @ColumnDef(
      value = "blob_value",
      type = "blob"
  )
  public final transient KeyValue<BlobValueDataMap> blobValue = new KeyValue(this, "blob_value", "blobValue", supplier);

  @ColumnDef(
      value = "max",
      type = "bigint(21)"
  )
  public final transient KeyValue<BlobValueDataMap> max = new KeyValue(this, "max", "max", supplier);

  @ColumnDef(
      value = "min",
      type = "bigint(21)"
  )
  public final transient KeyValue<BlobValueDataMap> min = new KeyValue(this, "min", "min", supplier);

  @ColumnDef(
      value = "origin",
      type = "varchar(20)"
  )
  public final transient KeyValue<BlobValueDataMap> origin = new KeyValue(this, "origin", "origin", supplier);

  BlobValueDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  BlobValueDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建BlobValueDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public BlobValueDataMap init() {
    this.id.autoIncrease();
    return this;
  }

  public BlobValueDataMap with(Consumer<BlobValueDataMap> init) {
    init.accept(this);
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

  /**
   * DataMap数据和表[blob_value]数据比较
   */
  public BlobValueDataMap eqTable(EqMode... modes) {
    IDatabase.db.table("blob_value").query().eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[blob_value]数据比较
   */
  public BlobValueDataMap eqQuery(String query, EqMode... modes) {
    IDatabase.db.table("blob_value").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[blob_value]数据比较
   */
  public BlobValueDataMap eqQuery(IDataMap query, EqMode... modes) {
    IDatabase.db.table("blob_value").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * 清空[blob_value]表数据
   */
  public BlobValueDataMap clean() {
    IDatabase.db.cleanTable("blob_value");
    return this;
  }

  /**
   * 插入[blob_value]表数据
   */
  public BlobValueDataMap insert() {
    IDatabase.db.table("blob_value").insert(this);
    return this;
  }

  /**
   * 先清空, 再插入[blob_value]表数据
   */
  public BlobValueDataMap cleanAndInsert() {
    return this.clean().insert();
  }

  public static class Factory {
    public BlobValueDataMap table() {
      return BlobValueDataMap.table();
    }

    public BlobValueDataMap table(int size) {
      return  BlobValueDataMap.table(size);
    }

    public BlobValueDataMap initTable() {
      return BlobValueDataMap.table().init();
    }

    public BlobValueDataMap initTable(int size) {
      return  BlobValueDataMap.table(size).init();
    }

    public BlobValueDataMap entity() {
      return BlobValueDataMap.entity();
    }

    public BlobValueDataMap entity(int size) {
      return  BlobValueDataMap.entity(size);
    }
  }
}
