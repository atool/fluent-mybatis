package cn.org.atool.fluent.mybatis.generator.shared4.dm;

import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * MyEnumTypeDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("my_enum_type")
@SuppressWarnings({"unused"})
public class MyEnumTypeDataMap extends TableDataMap<MyEnumTypeDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<MyEnumTypeDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "enum-num",
      type = "INT"
  )
  public final transient KeyValue<MyEnumTypeDataMap> enumNum = new KeyValue<>(this, "enum-num", "enumNum", supplier);

  @ColumnDef(
      value = "enum_string",
      type = "VARCHAR(20)"
  )
  public final transient KeyValue<MyEnumTypeDataMap> enumString = new KeyValue<>(this, "enum_string", "enumString", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "TINYINT",
      defaultValue = "0"
  )
  public final transient KeyValue<MyEnumTypeDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  MyEnumTypeDataMap(boolean isTable) {
    super("my_enum_type", isTable);
  }

  MyEnumTypeDataMap(boolean isTable, int size) {
    super("my_enum_type", isTable, size);
  }

  /**
   * 创建MyEnumTypeDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public MyEnumTypeDataMap init() {
    this.id.autoIncrease();
    this.isDeleted.values(false);
    return this;
  }

  public static MyEnumTypeDataMap table() {
    return new MyEnumTypeDataMap(true, 1);
  }

  public static MyEnumTypeDataMap table(int size) {
    return new MyEnumTypeDataMap(true, size);
  }

  public static MyEnumTypeDataMap entity() {
    return new MyEnumTypeDataMap(false, 1);
  }

  public static MyEnumTypeDataMap entity(int size) {
    return new MyEnumTypeDataMap(false, size);
  }

  public static class Factory extends BaseFactory<MyEnumTypeDataMap> {
    public Factory() {
      super(MyEnumTypeDataMap.class);
    }
  }
}
