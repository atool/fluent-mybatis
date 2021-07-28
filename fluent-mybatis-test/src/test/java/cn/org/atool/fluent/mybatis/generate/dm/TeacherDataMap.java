package cn.org.atool.fluent.mybatis.generate.dm;

import java.lang.Boolean;
import java.lang.String;
import java.lang.SuppressWarnings;
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
 * TeacherDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("teacher")
@SuppressWarnings({"unused", "rawtypes"})
public class TeacherDataMap extends DataMap<TeacherDataMap> {
  private boolean isTable;

  private final Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "id",
      type = "BIGINT",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<TeacherDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "DATETIME"
  )
  public final transient KeyValue<TeacherDataMap> gmtCreated = new KeyValue<>(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "DATETIME"
  )
  public final transient KeyValue<TeacherDataMap> gmtModified = new KeyValue<>(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "TINYINT",
      defaultValue = "0"
  )
  public final transient KeyValue<TeacherDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  @ColumnDef(
      value = "env",
      type = "VARCHAR(10)"
  )
  public final transient KeyValue<TeacherDataMap> env = new KeyValue<>(this, "env", "env", supplier);

  @ColumnDef(
      value = "user_name",
      type = "VARCHAR(20)"
  )
  public final transient KeyValue<TeacherDataMap> userName = new KeyValue<>(this, "user_name", "userName", supplier);

  TeacherDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  TeacherDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建TeacherDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public TeacherDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public TeacherDataMap with(Consumer<TeacherDataMap> init) {
    init.accept(this);
    return this;
  }

  public static TeacherDataMap table() {
    return new TeacherDataMap(true, 1);
  }

  public static TeacherDataMap table(int size) {
    return new TeacherDataMap(true, size);
  }

  public static TeacherDataMap entity() {
    return new TeacherDataMap(false, 1);
  }

  public static TeacherDataMap entity(int size) {
    return new TeacherDataMap(false, size);
  }

  /**
   * DataMap数据和表[teacher]数据比较
   */
  public TeacherDataMap eqTable(EqMode... modes) {
    IDatabase.db.table("teacher").query().eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[teacher]数据比较
   */
  public TeacherDataMap eqQuery(String query, EqMode... modes) {
    IDatabase.db.table("teacher").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[teacher]数据比较
   */
  public TeacherDataMap eqQuery(IDataMap query, EqMode... modes) {
    IDatabase.db.table("teacher").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * 清空[teacher]表数据
   */
  public TeacherDataMap clean() {
    IDatabase.db.cleanTable("teacher");
    return this;
  }

  /**
   * 插入[teacher]表数据
   */
  public TeacherDataMap insert() {
    IDatabase.db.table("teacher").insert(this);
    return this;
  }

  /**
   * 先清空, 再插入[teacher]表数据
   */
  public TeacherDataMap cleanAndInsert() {
    return this.clean().insert();
  }

  public static class Factory {
    public TeacherDataMap table() {
      return TeacherDataMap.table();
    }

    public TeacherDataMap table(int size) {
      return  TeacherDataMap.table(size);
    }

    public TeacherDataMap initTable() {
      return TeacherDataMap.table().init();
    }

    public TeacherDataMap initTable(int size) {
      return  TeacherDataMap.table(size).init();
    }

    public TeacherDataMap entity() {
      return TeacherDataMap.entity();
    }

    public TeacherDataMap entity(int size) {
      return  TeacherDataMap.entity(size);
    }
  }
}
