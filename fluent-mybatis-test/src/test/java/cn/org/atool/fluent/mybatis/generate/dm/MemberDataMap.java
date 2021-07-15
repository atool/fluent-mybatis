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
 * MemberDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("t_member")
public class MemberDataMap extends DataMap<MemberDataMap> {
  private boolean isTable;

  private Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "id",
      type = "BIGINT UNSIGNED",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<MemberDataMap> id = new KeyValue(this, "id", "id", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "DATETIME"
  )
  public final transient KeyValue<MemberDataMap> gmtModified = new KeyValue(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "BIT",
      defaultValue = "0"
  )
  public final transient KeyValue<MemberDataMap> isDeleted = new KeyValue(this, "is_deleted", "isDeleted", supplier);

  @ColumnDef(
      value = "age",
      type = "INT"
  )
  public final transient KeyValue<MemberDataMap> age = new KeyValue(this, "age", "age", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "DATETIME"
  )
  public final transient KeyValue<MemberDataMap> gmtCreated = new KeyValue(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "is_girl",
      type = "BIT",
      defaultValue = "0"
  )
  public final transient KeyValue<MemberDataMap> isGirl = new KeyValue(this, "is_girl", "isGirl", supplier);

  @ColumnDef(
      value = "school",
      type = "VARCHAR(20)"
  )
  public final transient KeyValue<MemberDataMap> school = new KeyValue(this, "school", "school", supplier);

  @ColumnDef(
      value = "user_name",
      type = "VARCHAR(45)"
  )
  public final transient KeyValue<MemberDataMap> userName = new KeyValue(this, "user_name", "userName", supplier);

  MemberDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  MemberDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建MemberDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public MemberDataMap init() {
    this.id.autoIncrease();
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public MemberDataMap with(Consumer<MemberDataMap> init) {
    init.accept(this);
    return this;
  }

  public static MemberDataMap table() {
    return new MemberDataMap(true, 1);
  }

  public static MemberDataMap table(int size) {
    return new MemberDataMap(true, size);
  }

  public static MemberDataMap entity() {
    return new MemberDataMap(false, 1);
  }

  public static MemberDataMap entity(int size) {
    return new MemberDataMap(false, size);
  }

  /**
   * DataMap数据和表[t_member]数据比较
   */
  public MemberDataMap eqTable(EqMode... modes) {
    IDatabase.db.table("t_member").query().eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[t_member]数据比较
   */
  public MemberDataMap eqQuery(String query, EqMode... modes) {
    IDatabase.db.table("t_member").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[t_member]数据比较
   */
  public MemberDataMap eqQuery(IDataMap query, EqMode... modes) {
    IDatabase.db.table("t_member").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * 清空[t_member]表数据
   */
  public MemberDataMap clean() {
    IDatabase.db.cleanTable("t_member");
    return this;
  }

  /**
   * 插入[t_member]表数据
   */
  public MemberDataMap insert() {
    IDatabase.db.table("t_member").insert(this);
    return this;
  }

  /**
   * 先清空, 再插入[t_member]表数据
   */
  public MemberDataMap cleanAndInsert() {
    return this.clean().insert();
  }

  public static class Factory {
    public MemberDataMap table() {
      return MemberDataMap.table();
    }

    public MemberDataMap table(int size) {
      return  MemberDataMap.table(size);
    }

    public MemberDataMap initTable() {
      return MemberDataMap.table().init();
    }

    public MemberDataMap initTable(int size) {
      return  MemberDataMap.table(size).init();
    }

    public MemberDataMap entity() {
      return MemberDataMap.entity();
    }

    public MemberDataMap entity(int size) {
      return  MemberDataMap.entity(size);
    }
  }
}
