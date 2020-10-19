package cn.org.atool.fluent.mybatis.generate.dm;

import java.lang.Boolean;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.test4j.module.ICore.DataMap;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.tools.datagen.KeyValue;

/**
 * AddressDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("address")
public class AddressDataMap extends DataMap<AddressDataMap> {
  private boolean isTable;

  private Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "id",
      type = "bigint(21) unsigned",
      primary = true,
      autoIncrease = true
  )
  public final transient KeyValue<AddressDataMap> id = new KeyValue(this, "id", "id", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "datetime"
  )
  public final transient KeyValue<AddressDataMap> gmtCreated = new KeyValue(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "datetime"
  )
  public final transient KeyValue<AddressDataMap> gmtModified = new KeyValue(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "tinyint(2)"
  )
  public final transient KeyValue<AddressDataMap> isDeleted = new KeyValue(this, "is_deleted", "isDeleted", supplier);

  @ColumnDef(
      value = "address",
      type = "varchar(45)"
  )
  public final transient KeyValue<AddressDataMap> address = new KeyValue(this, "address", "address", supplier);

  @ColumnDef(
      value = "user_id",
      type = "bigint(20)"
  )
  public final transient KeyValue<AddressDataMap> userId = new KeyValue(this, "user_id", "userId", supplier);

  AddressDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  AddressDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建AddressDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public AddressDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public AddressDataMap with(Consumer<AddressDataMap> init) {
    init.accept(this);
    return this;
  }

  public static AddressDataMap table() {
    return new AddressDataMap(true, 1);
  }

  public static AddressDataMap table(int size) {
    return new AddressDataMap(true, size);
  }

  public static AddressDataMap entity() {
    return new AddressDataMap(false, 1);
  }

  public static AddressDataMap entity(int size) {
    return new AddressDataMap(false, size);
  }

  public static class Factory {
    public AddressDataMap table() {
      return AddressDataMap.table();
    }

    public AddressDataMap table(int size) {
      return  AddressDataMap.table(size);
    }

    public AddressDataMap initTable() {
      return AddressDataMap.table().init();
    }

    public AddressDataMap initTable(int size) {
      return  AddressDataMap.table(size).init();
    }

    public AddressDataMap entity() {
      return AddressDataMap.entity();
    }

    public AddressDataMap entity(int size) {
      return  AddressDataMap.entity(size);
    }
  }
}
