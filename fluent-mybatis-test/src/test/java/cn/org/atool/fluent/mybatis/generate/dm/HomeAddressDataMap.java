package cn.org.atool.fluent.mybatis.generate.dm;

import java.lang.Boolean;
import java.lang.String;
import java.util.Date;
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
 * HomeAddressDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("home_address")
public class HomeAddressDataMap extends DataMap<HomeAddressDataMap> {
  private boolean isTable;

  private Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "id",
      type = "bigint(21) unsigned",
      primary = true,
      autoIncrease = true
  )
  public final transient KeyValue<HomeAddressDataMap> id = new KeyValue(this, "id", "id", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "datetime"
  )
  public final transient KeyValue<HomeAddressDataMap> gmtCreated = new KeyValue(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "datetime"
  )
  public final transient KeyValue<HomeAddressDataMap> gmtModified = new KeyValue(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "tinyint(2)"
  )
  public final transient KeyValue<HomeAddressDataMap> isDeleted = new KeyValue(this, "is_deleted", "isDeleted", supplier);

  @ColumnDef(
      value = "address",
      type = "varchar(100)"
  )
  public final transient KeyValue<HomeAddressDataMap> address = new KeyValue(this, "address", "address", supplier);

  @ColumnDef(
      value = "city",
      type = "varchar(50)"
  )
  public final transient KeyValue<HomeAddressDataMap> city = new KeyValue(this, "city", "city", supplier);

  @ColumnDef(
      value = "district",
      type = "varchar(50)"
  )
  public final transient KeyValue<HomeAddressDataMap> district = new KeyValue(this, "district", "district", supplier);

  @ColumnDef(
      value = "env",
      type = "varchar(10)"
  )
  public final transient KeyValue<HomeAddressDataMap> env = new KeyValue(this, "env", "env", supplier);

  @ColumnDef(
      value = "province",
      type = "varchar(50)"
  )
  public final transient KeyValue<HomeAddressDataMap> province = new KeyValue(this, "province", "province", supplier);

  @ColumnDef(
      value = "student_id",
      type = "bigint(21)"
  )
  public final transient KeyValue<HomeAddressDataMap> studentId = new KeyValue(this, "student_id", "studentId", supplier);

  @ColumnDef(
      value = "tenant",
      type = "bigint(20)"
  )
  public final transient KeyValue<HomeAddressDataMap> tenant = new KeyValue(this, "tenant", "tenant", supplier);

  HomeAddressDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  HomeAddressDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建HomeAddressDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public HomeAddressDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public HomeAddressDataMap with(Consumer<HomeAddressDataMap> init) {
    init.accept(this);
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

  /**
   * DataMap数据和表[home_address]数据比较
   */
  public HomeAddressDataMap eqTable(EqMode... modes) {
    IDatabase.db.table("home_address").query().eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[home_address]数据比较
   */
  public HomeAddressDataMap eqQuery(String query, EqMode... modes) {
    IDatabase.db.table("home_address").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[home_address]数据比较
   */
  public HomeAddressDataMap eqQuery(IDataMap query, EqMode... modes) {
    IDatabase.db.table("home_address").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * 清空[home_address]表数据
   */
  public HomeAddressDataMap clean() {
    IDatabase.db.cleanTable("home_address");
    return this;
  }

  /**
   * 插入[home_address]表数据
   */
  public HomeAddressDataMap insert() {
    IDatabase.db.table("home_address").insert(this);
    return this;
  }

  /**
   * 先清空, 再插入[home_address]表数据
   */
  public HomeAddressDataMap cleanAndInsert() {
    return this.clean().insert();
  }

  public static class Factory {
    public HomeAddressDataMap table() {
      return HomeAddressDataMap.table();
    }

    public HomeAddressDataMap table(int size) {
      return  HomeAddressDataMap.table(size);
    }

    public HomeAddressDataMap initTable() {
      return HomeAddressDataMap.table().init();
    }

    public HomeAddressDataMap initTable(int size) {
      return  HomeAddressDataMap.table(size).init();
    }

    public HomeAddressDataMap entity() {
      return HomeAddressDataMap.entity();
    }

    public HomeAddressDataMap entity(int size) {
      return  HomeAddressDataMap.entity(size);
    }
  }
}
