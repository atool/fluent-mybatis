package cn.org.atool.fluent.mybatis.generate.dm;

import java.lang.Boolean;
import java.lang.String;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.test4j.module.ICore.DataMap;
import org.test4j.module.database.IDatabase;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.tools.datagen.IDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * StudentDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("student")
public class StudentDataMap extends DataMap<StudentDataMap> {
  private boolean isTable;

  private Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "id",
      type = "bigint(21) unsigned",
      primary = true,
      autoIncrease = true
  )
  public final transient KeyValue<StudentDataMap> id = new KeyValue(this, "id", "id", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "datetime"
  )
  public final transient KeyValue<StudentDataMap> gmtCreated = new KeyValue(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "datetime"
  )
  public final transient KeyValue<StudentDataMap> gmtModified = new KeyValue(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "tinyint(2)"
  )
  public final transient KeyValue<StudentDataMap> isDeleted = new KeyValue(this, "is_deleted", "isDeleted", supplier);

  @ColumnDef(
      value = "address",
      type = "varchar(200)"
  )
  public final transient KeyValue<StudentDataMap> address = new KeyValue(this, "address", "address", supplier);

  @ColumnDef(
      value = "age",
      type = "int(11)"
  )
  public final transient KeyValue<StudentDataMap> age = new KeyValue(this, "age", "age", supplier);

  @ColumnDef(
      value = "birthday",
      type = "datetime"
  )
  public final transient KeyValue<StudentDataMap> birthday = new KeyValue(this, "birthday", "birthday", supplier);

  @ColumnDef(
      value = "bonus_points",
      type = "bigint(21)"
  )
  public final transient KeyValue<StudentDataMap> bonusPoints = new KeyValue(this, "bonus_points", "bonusPoints", supplier);

  @ColumnDef(
      value = "env",
      type = "varchar(10)"
  )
  public final transient KeyValue<StudentDataMap> env = new KeyValue(this, "env", "env", supplier);

  @ColumnDef(
      value = "gender_man",
      type = "tinyint(2)"
  )
  public final transient KeyValue<StudentDataMap> genderMan = new KeyValue(this, "gender_man", "genderMan", supplier);

  @ColumnDef(
      value = "grade",
      type = "int(11)"
  )
  public final transient KeyValue<StudentDataMap> grade = new KeyValue(this, "grade", "grade", supplier);

  @ColumnDef(
      value = "home_address_id",
      type = "bigint(21)"
  )
  public final transient KeyValue<StudentDataMap> homeAddressId = new KeyValue(this, "home_address_id", "homeAddressId", supplier);

  @ColumnDef(
      value = "home_county_id",
      type = "bigint(21)"
  )
  public final transient KeyValue<StudentDataMap> homeCountyId = new KeyValue(this, "home_county_id", "homeCountyId", supplier);

  @ColumnDef(
      value = "phone",
      type = "varchar(20)"
  )
  public final transient KeyValue<StudentDataMap> phone = new KeyValue(this, "phone", "phone", supplier);

  @ColumnDef(
      value = "status",
      type = "varchar(32)"
  )
  public final transient KeyValue<StudentDataMap> status = new KeyValue(this, "status", "status", supplier);

  @ColumnDef(
      value = "tenant",
      type = "bigint(20)"
  )
  public final transient KeyValue<StudentDataMap> tenant = new KeyValue(this, "tenant", "tenant", supplier);

  @ColumnDef(
      value = "user_name",
      type = "varchar(45)"
  )
  public final transient KeyValue<StudentDataMap> userName = new KeyValue(this, "user_name", "userName", supplier);

  @ColumnDef(
      value = "version",
      type = "varchar(200)"
  )
  public final transient KeyValue<StudentDataMap> version = new KeyValue(this, "version", "version", supplier);

  StudentDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  StudentDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建StudentDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public StudentDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public StudentDataMap with(Consumer<StudentDataMap> init) {
    init.accept(this);
    return this;
  }

  public static StudentDataMap table() {
    return new StudentDataMap(true, 1);
  }

  public static StudentDataMap table(int size) {
    return new StudentDataMap(true, size);
  }

  public static StudentDataMap entity() {
    return new StudentDataMap(false, 1);
  }

  public static StudentDataMap entity(int size) {
    return new StudentDataMap(false, size);
  }

  /**
   * DataMap数据和表[student]数据比较
   */
  public StudentDataMap eqTable() {
    IDatabase.db.table("student").query().eqDataMap(this);
    return this;
  }

  /**
   * DataMap数据和表[student]数据比较
   */
  public StudentDataMap eqQuery(String query) {
    IDatabase.db.table("student").queryWhere(query).eqDataMap(this);
    return this;
  }

  /**
   * DataMap数据和表[student]数据比较
   */
  public StudentDataMap eqQuery(IDataMap query) {
    IDatabase.db.table("student").queryWhere(query).eqDataMap(this);
    return this;
  }

  /**
   * 清空[student]表数据
   */
  public StudentDataMap clean() {
    IDatabase.db.cleanTable("student");
    return this;
  }

  /**
   * 插入[student]表数据
   */
  public StudentDataMap insert() {
    IDatabase.db.table("student").insert(this);
    return this;
  }

  /**
   * 先清空, 再插入[student]表数据
   */
  public StudentDataMap cleanAndInsert() {
    return this.clean().insert();
  }

  public static class Factory {
    public StudentDataMap table() {
      return StudentDataMap.table();
    }

    public StudentDataMap table(int size) {
      return  StudentDataMap.table(size);
    }

    public StudentDataMap initTable() {
      return StudentDataMap.table().init();
    }

    public StudentDataMap initTable(int size) {
      return  StudentDataMap.table(size).init();
    }

    public StudentDataMap entity() {
      return StudentDataMap.entity();
    }

    public StudentDataMap entity(int size) {
      return  StudentDataMap.entity(size);
    }
  }
}
