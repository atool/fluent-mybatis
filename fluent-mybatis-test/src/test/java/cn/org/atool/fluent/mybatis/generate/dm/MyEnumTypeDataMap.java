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
 * MyEnumTypeDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("my_enum_type")
public class MyEnumTypeDataMap extends DataMap<MyEnumTypeDataMap> {
  private boolean isTable;

  private Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "id",
      type = "bigint(20)",
      primary = true,
      autoIncrease = true
  )
  public final transient KeyValue<MyEnumTypeDataMap> id = new KeyValue(this, "id", "id", supplier);

  @ColumnDef(
      value = "enum_num",
      type = "int(11)"
  )
  public final transient KeyValue<MyEnumTypeDataMap> enumNum = new KeyValue(this, "enum_num", "enumNum", supplier);

  @ColumnDef(
      value = "enum_string",
      type = "varchar(20)"
  )
  public final transient KeyValue<MyEnumTypeDataMap> enumString = new KeyValue(this, "enum_string", "enumString", supplier);

  MyEnumTypeDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  MyEnumTypeDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建MyEnumTypeDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public MyEnumTypeDataMap init() {
    this.id.autoIncrease();
    return this;
  }

  public MyEnumTypeDataMap with(Consumer<MyEnumTypeDataMap> init) {
    init.accept(this);
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

  /**
   * DataMap数据和表[my_enum_type]数据比较
   */
  public MyEnumTypeDataMap eqTable(EqMode... modes) {
    IDatabase.db.table("my_enum_type").query().eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[my_enum_type]数据比较
   */
  public MyEnumTypeDataMap eqQuery(String query, EqMode... modes) {
    IDatabase.db.table("my_enum_type").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[my_enum_type]数据比较
   */
  public MyEnumTypeDataMap eqQuery(IDataMap query, EqMode... modes) {
    IDatabase.db.table("my_enum_type").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * 清空[my_enum_type]表数据
   */
  public MyEnumTypeDataMap clean() {
    IDatabase.db.cleanTable("my_enum_type");
    return this;
  }

  /**
   * 插入[my_enum_type]表数据
   */
  public MyEnumTypeDataMap insert() {
    IDatabase.db.table("my_enum_type").insert(this);
    return this;
  }

  /**
   * 先清空, 再插入[my_enum_type]表数据
   */
  public MyEnumTypeDataMap cleanAndInsert() {
    return this.clean().insert();
  }

  public static class Factory {
    public MyEnumTypeDataMap table() {
      return MyEnumTypeDataMap.table();
    }

    public MyEnumTypeDataMap table(int size) {
      return  MyEnumTypeDataMap.table(size);
    }

    public MyEnumTypeDataMap initTable() {
      return MyEnumTypeDataMap.table().init();
    }

    public MyEnumTypeDataMap initTable(int size) {
      return  MyEnumTypeDataMap.table(size).init();
    }

    public MyEnumTypeDataMap entity() {
      return MyEnumTypeDataMap.entity();
    }

    public MyEnumTypeDataMap entity(int size) {
      return  MyEnumTypeDataMap.entity(size);
    }
  }
}
