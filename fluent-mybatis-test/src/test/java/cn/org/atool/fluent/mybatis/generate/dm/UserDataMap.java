package cn.org.atool.fluent.mybatis.generate.dm;

import org.test4j.module.ICore.DataMap;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * UserDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("t_user")
public class UserDataMap extends DataMap<UserDataMap> {
  private boolean isTable;

  private Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "id",
      type = "bigint(21) unsigned",
      primary = true,
      autoIncrease = true
  )
  public final transient KeyValue<UserDataMap> id = new KeyValue(this, "id", "id", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "datetime"
  )
  public final transient KeyValue<UserDataMap> gmtCreated = new KeyValue(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "datetime"
  )
  public final transient KeyValue<UserDataMap> gmtModified = new KeyValue(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "tinyint(2)"
  )
  public final transient KeyValue<UserDataMap> isDeleted = new KeyValue(this, "is_deleted", "isDeleted", supplier);

  @ColumnDef(
      value = "account",
      type = "varchar(45)"
  )
  public final transient KeyValue<UserDataMap> account = new KeyValue(this, "account", "account", supplier);

  @ColumnDef(
      value = "address_id",
      type = "bigint(21)"
  )
  public final transient KeyValue<UserDataMap> addressId = new KeyValue(this, "address_id", "addressId", supplier);

  @ColumnDef(
      value = "age",
      type = "int(11)"
  )
  public final transient KeyValue<UserDataMap> age = new KeyValue(this, "age", "age", supplier);

  @ColumnDef(
      value = "avatar",
      type = "varchar(255)"
  )
  public final transient KeyValue<UserDataMap> avatar = new KeyValue(this, "avatar", "avatar", supplier);

  @ColumnDef(
      value = "birthday",
      type = "datetime"
  )
  public final transient KeyValue<UserDataMap> birthday = new KeyValue(this, "birthday", "birthday", supplier);

  @ColumnDef(
      value = "bonus_points",
      type = "bigint(21)"
  )
  public final transient KeyValue<UserDataMap> bonusPoints = new KeyValue(this, "bonus_points", "bonusPoints", supplier);

  @ColumnDef(
      value = "e_mail",
      type = "varchar(45)"
  )
  public final transient KeyValue<UserDataMap> eMail = new KeyValue(this, "e_mail", "eMail", supplier);

  @ColumnDef(
      value = "grade",
      type = "int(11)"
  )
  public final transient KeyValue<UserDataMap> grade = new KeyValue(this, "grade", "grade", supplier);

  @ColumnDef(
      value = "password",
      type = "varchar(45)"
  )
  public final transient KeyValue<UserDataMap> password = new KeyValue(this, "password", "password", supplier);

  @ColumnDef(
      value = "phone",
      type = "varchar(20)"
  )
  public final transient KeyValue<UserDataMap> phone = new KeyValue(this, "phone", "phone", supplier);

  @ColumnDef(
      value = "status",
      type = "varchar(32)"
  )
  public final transient KeyValue<UserDataMap> status = new KeyValue(this, "status", "status", supplier);

  @ColumnDef(
      value = "user_name",
      type = "varchar(45)"
  )
  public final transient KeyValue<UserDataMap> userName = new KeyValue(this, "user_name", "userName", supplier);

  @ColumnDef(
      value = "version",
      type = "varchar(45)"
  )
  public final transient KeyValue<UserDataMap> version = new KeyValue(this, "version", "version", supplier);

  UserDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  UserDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建UserDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public UserDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public UserDataMap with(Consumer<UserDataMap> init) {
    init.accept(this);
    return this;
  }

  public static UserDataMap table() {
    return new UserDataMap(true, 1);
  }

  public static UserDataMap table(int size) {
    return new UserDataMap(true, size);
  }

  public static UserDataMap entity() {
    return new UserDataMap(false, 1);
  }

  public static UserDataMap entity(int size) {
    return new UserDataMap(false, size);
  }

  public static class Factory {
    public UserDataMap table() {
      return UserDataMap.table();
    }

    public UserDataMap table(int size) {
      return  UserDataMap.table(size);
    }

    public UserDataMap initTable() {
      return UserDataMap.table().init();
    }

    public UserDataMap initTable(int size) {
      return  UserDataMap.table(size).init();
    }

    public UserDataMap entity() {
      return UserDataMap.entity();
    }

    public UserDataMap entity(int size) {
      return  UserDataMap.entity(size);
    }
  }
}
