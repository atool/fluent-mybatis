package cn.org.atool.fluent.mybatis.generator.shared2.dm;

import java.util.Date;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * HomeAddressDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("home_address")
@SuppressWarnings({"unused"})
public class HomeAddressDataMap extends TableDataMap<HomeAddressDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT UNSIGNED",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<HomeAddressDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "address",
      type = "VARCHAR(100)"
  )
  public final transient KeyValue<HomeAddressDataMap> address = new KeyValue<>(this, "address", "address", supplier);

  @ColumnDef(
      value = "city",
      type = "VARCHAR(50)"
  )
  public final transient KeyValue<HomeAddressDataMap> city = new KeyValue<>(this, "city", "city", supplier);

  @ColumnDef(
      value = "district",
      type = "VARCHAR(50)"
  )
  public final transient KeyValue<HomeAddressDataMap> district = new KeyValue<>(this, "district", "district", supplier);

  @ColumnDef(
      value = "env",
      type = "VARCHAR(10)"
  )
  public final transient KeyValue<HomeAddressDataMap> env = new KeyValue<>(this, "env", "env", supplier);

  @ColumnDef(
      value = "province",
      type = "VARCHAR(50)"
  )
  public final transient KeyValue<HomeAddressDataMap> province = new KeyValue<>(this, "province", "province", supplier);

  @ColumnDef(
      value = "student_id",
      type = "BIGINT",
      notNull = true
  )
  public final transient KeyValue<HomeAddressDataMap> studentId = new KeyValue<>(this, "student_id", "studentId", supplier);

  @ColumnDef(
      value = "tenant",
      type = "BIGINT",
      notNull = true,
      defaultValue = "0"
  )
  public final transient KeyValue<HomeAddressDataMap> tenant = new KeyValue<>(this, "tenant", "tenant", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "DATETIME"
  )
  public final transient KeyValue<HomeAddressDataMap> gmtCreated = new KeyValue<>(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "DATETIME"
  )
  public final transient KeyValue<HomeAddressDataMap> gmtModified = new KeyValue<>(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "TINYINT",
      defaultValue = "0"
  )
  public final transient KeyValue<HomeAddressDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  HomeAddressDataMap(boolean isTable) {
    super("home_address", isTable);
  }

  HomeAddressDataMap(boolean isTable, int size) {
    super("home_address", isTable, size);
  }

  /**
   * 创建HomeAddressDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public HomeAddressDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public static HomeAddressDataMap table() {
    return new HomeAddressDataMap(true, 1);
  }

  public static HomeAddressDataMap table(int size) {
    return new HomeAddressDataMap(true, size);
  }

  public static HomeAddressDataMap entity() {
    return new HomeAddressDataMap(false, 1);
  }

  public static HomeAddressDataMap entity(int size) {
    return new HomeAddressDataMap(false, size);
  }

  public static class Factory extends BaseFactory<HomeAddressDataMap> {
    public Factory() {
      super(HomeAddressDataMap.class);
    }
  }
}
