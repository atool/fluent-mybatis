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
 * MemberFavoriteDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("t_member_favorite")
@SuppressWarnings({"unused"})
public class MemberFavoriteDataMap extends DataMap<MemberFavoriteDataMap> {
  private boolean isTable;

  private final Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "id",
      type = "BIGINT UNSIGNED",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<MemberFavoriteDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "DATETIME"
  )
  public final transient KeyValue<MemberFavoriteDataMap> gmtModified = new KeyValue<>(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "TINYINT",
      defaultValue = "0"
  )
  public final transient KeyValue<MemberFavoriteDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  @ColumnDef(
      value = "favorite",
      type = "VARCHAR(45)"
  )
  public final transient KeyValue<MemberFavoriteDataMap> favorite = new KeyValue<>(this, "favorite", "favorite", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "DATETIME"
  )
  public final transient KeyValue<MemberFavoriteDataMap> gmtCreated = new KeyValue<>(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "member_id",
      type = "BIGINT",
      notNull = true
  )
  public final transient KeyValue<MemberFavoriteDataMap> memberId = new KeyValue<>(this, "member_id", "memberId", supplier);

  MemberFavoriteDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  MemberFavoriteDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建MemberFavoriteDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public MemberFavoriteDataMap init() {
    this.id.autoIncrease();
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public MemberFavoriteDataMap with(Consumer<MemberFavoriteDataMap> init) {
    init.accept(this);
    return this;
  }

  public static MemberFavoriteDataMap table() {
    return new MemberFavoriteDataMap(true, 1);
  }

  public static MemberFavoriteDataMap table(int size) {
    return new MemberFavoriteDataMap(true, size);
  }

  public static MemberFavoriteDataMap entity() {
    return new MemberFavoriteDataMap(false, 1);
  }

  public static MemberFavoriteDataMap entity(int size) {
    return new MemberFavoriteDataMap(false, size);
  }

  /**
   * DataMap数据和表[t_member_favorite]数据比较
   */
  public MemberFavoriteDataMap eqTable(EqMode... modes) {
    IDatabase.db.table("t_member_favorite").query().eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[t_member_favorite]数据比较
   */
  public MemberFavoriteDataMap eqQuery(String query, EqMode... modes) {
    IDatabase.db.table("t_member_favorite").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[t_member_favorite]数据比较
   */
  public MemberFavoriteDataMap eqQuery(IDataMap query, EqMode... modes) {
    IDatabase.db.table("t_member_favorite").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * 清空[t_member_favorite]表数据
   */
  public MemberFavoriteDataMap clean() {
    IDatabase.db.cleanTable("t_member_favorite");
    return this;
  }

  /**
   * 插入[t_member_favorite]表数据
   */
  public MemberFavoriteDataMap insert() {
    IDatabase.db.table("t_member_favorite").insert(this);
    return this;
  }

  /**
   * 先清空, 再插入[t_member_favorite]表数据
   */
  public MemberFavoriteDataMap cleanAndInsert() {
    return this.clean().insert();
  }

  public static class Factory {
    public MemberFavoriteDataMap table() {
      return MemberFavoriteDataMap.table();
    }

    public MemberFavoriteDataMap table(int size) {
      return  MemberFavoriteDataMap.table(size);
    }

    public MemberFavoriteDataMap initTable() {
      return MemberFavoriteDataMap.table().init();
    }

    public MemberFavoriteDataMap initTable(int size) {
      return  MemberFavoriteDataMap.table(size).init();
    }

    public MemberFavoriteDataMap entity() {
      return MemberFavoriteDataMap.entity();
    }

    public MemberFavoriteDataMap entity(int size) {
      return  MemberFavoriteDataMap.entity(size);
    }
  }
}
