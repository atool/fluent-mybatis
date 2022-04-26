package cn.org.atool.fluent.mybatis.generator.shared2.dm;

import java.util.Date;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * StudentDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("student")
@SuppressWarnings({"unused"})
public class StudentDataMap extends TableDataMap<StudentDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT UNSIGNED",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<StudentDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "address",
      type = "VARCHAR(200)"
  )
  public final transient KeyValue<StudentDataMap> address = new KeyValue<>(this, "address", "address", supplier);

  @ColumnDef(
      value = "age",
      type = "INT"
  )
  public final transient KeyValue<StudentDataMap> age = new KeyValue<>(this, "age", "age", supplier);

  @ColumnDef(
      value = "birthday",
      type = "DATETIME"
  )
  public final transient KeyValue<StudentDataMap> birthday = new KeyValue<>(this, "birthday", "birthday", supplier);

  @ColumnDef(
      value = "bonus_points",
      type = "BIGINT",
      defaultValue = "0"
  )
  public final transient KeyValue<StudentDataMap> bonusPoints = new KeyValue<>(this, "bonus_points", "bonusPoints", supplier);

  @ColumnDef(
      value = "desk_mate_id",
      type = "BIGINT"
  )
  public final transient KeyValue<StudentDataMap> deskMateId = new KeyValue<>(this, "desk_mate_id", "deskMateId", supplier);

  @ColumnDef(
      value = "email",
      type = "VARCHAR(50)"
  )
  public final transient KeyValue<StudentDataMap> email = new KeyValue<>(this, "email", "email", supplier);

  @ColumnDef(
      value = "env",
      type = "VARCHAR(10)"
  )
  public final transient KeyValue<StudentDataMap> env = new KeyValue<>(this, "env", "env", supplier);

  @ColumnDef(
      value = "gender",
      type = "TINYINT",
      defaultValue = "0"
  )
  public final transient KeyValue<StudentDataMap> gender = new KeyValue<>(this, "gender", "gender", supplier);

  @ColumnDef(
      value = "grade",
      type = "INT"
  )
  public final transient KeyValue<StudentDataMap> grade = new KeyValue<>(this, "grade", "grade", supplier);

  @ColumnDef(
      value = "home_address_id",
      type = "BIGINT"
  )
  public final transient KeyValue<StudentDataMap> homeAddressId = new KeyValue<>(this, "home_address_id", "homeAddressId", supplier);

  @ColumnDef(
      value = "home_county_id",
      type = "BIGINT"
  )
  public final transient KeyValue<StudentDataMap> homeCountyId = new KeyValue<>(this, "home_county_id", "homeCountyId", supplier);

  @ColumnDef(
      value = "phone",
      type = "VARCHAR(20)"
  )
  public final transient KeyValue<StudentDataMap> phone = new KeyValue<>(this, "phone", "phone", supplier);

  @ColumnDef(
      value = "status",
      type = "VARCHAR(32)"
  )
  public final transient KeyValue<StudentDataMap> status = new KeyValue<>(this, "status", "status", supplier);

  @ColumnDef(
      value = "tenant",
      type = "BIGINT",
      notNull = true,
      defaultValue = "0"
  )
  public final transient KeyValue<StudentDataMap> tenant = new KeyValue<>(this, "tenant", "tenant", supplier);

  @ColumnDef(
      value = "user_name",
      type = "VARCHAR(45)"
  )
  public final transient KeyValue<StudentDataMap> userName = new KeyValue<>(this, "user_name", "userName", supplier);

  @ColumnDef(
      value = "version",
      type = "VARCHAR(200)"
  )
  public final transient KeyValue<StudentDataMap> version = new KeyValue<>(this, "version", "version", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "DATETIME"
  )
  public final transient KeyValue<StudentDataMap> gmtCreated = new KeyValue<>(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "DATETIME"
  )
  public final transient KeyValue<StudentDataMap> gmtModified = new KeyValue<>(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "TINYINT",
      defaultValue = "0"
  )
  public final transient KeyValue<StudentDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  StudentDataMap(boolean isTable) {
    super("student", isTable);
  }

  StudentDataMap(boolean isTable, int size) {
    super("student", isTable, size);
  }

  /**
   * 创建StudentDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public StudentDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
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

  public static class Factory extends BaseFactory<StudentDataMap> {
    public Factory() {
      super(StudentDataMap.class);
    }
  }
}
