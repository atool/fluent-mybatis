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
 * MemberLoveDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("t_member_love")
public class MemberLoveDataMap extends DataMap<MemberLoveDataMap> {
  private boolean isTable;

  private Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "id",
      type = "bigint(21) unsigned",
      primary = true,
      autoIncrease = true
  )
  public final transient KeyValue<MemberLoveDataMap> id = new KeyValue(this, "id", "id", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "datetime"
  )
  public final transient KeyValue<MemberLoveDataMap> gmtModified = new KeyValue(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "tinyint(2)"
  )
  public final transient KeyValue<MemberLoveDataMap> isDeleted = new KeyValue(this, "is_deleted", "isDeleted", supplier);

  @ColumnDef(
      value = "boy_id",
      type = "bigint(21)"
  )
  public final transient KeyValue<MemberLoveDataMap> boyId = new KeyValue(this, "boy_id", "boyId", supplier);

  @ColumnDef(
      value = "girl_id",
      type = "bigint(21)"
  )
  public final transient KeyValue<MemberLoveDataMap> girlId = new KeyValue(this, "girl_id", "girlId", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "datetime"
  )
  public final transient KeyValue<MemberLoveDataMap> gmtCreated = new KeyValue(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "status",
      type = "varchar(45)"
  )
  public final transient KeyValue<MemberLoveDataMap> status = new KeyValue(this, "status", "status", supplier);

  MemberLoveDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  MemberLoveDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建MemberLoveDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public MemberLoveDataMap init() {
    this.id.autoIncrease();
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public MemberLoveDataMap with(Consumer<MemberLoveDataMap> init) {
    init.accept(this);
    return this;
  }

  public static MemberLoveDataMap table() {
    return new MemberLoveDataMap(true, 1);
  }

  public static MemberLoveDataMap table(int size) {
    return new MemberLoveDataMap(true, size);
  }

  public static MemberLoveDataMap entity() {
    return new MemberLoveDataMap(false, 1);
  }

  public static MemberLoveDataMap entity(int size) {
    return new MemberLoveDataMap(false, size);
  }

  /**
   * DataMap数据和表[t_member_love]数据比较
   */
  public MemberLoveDataMap eqTable(EqMode... modes) {
    IDatabase.db.table("t_member_love").query().eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[t_member_love]数据比较
   */
  public MemberLoveDataMap eqQuery(String query, EqMode... modes) {
    IDatabase.db.table("t_member_love").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[t_member_love]数据比较
   */
  public MemberLoveDataMap eqQuery(IDataMap query, EqMode... modes) {
    IDatabase.db.table("t_member_love").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * 清空[t_member_love]表数据
   */
  public MemberLoveDataMap clean() {
    IDatabase.db.cleanTable("t_member_love");
    return this;
  }

  /**
   * 插入[t_member_love]表数据
   */
  public MemberLoveDataMap insert() {
    IDatabase.db.table("t_member_love").insert(this);
    return this;
  }

  /**
   * 先清空, 再插入[t_member_love]表数据
   */
  public MemberLoveDataMap cleanAndInsert() {
    return this.clean().insert();
  }

  public static class Factory {
    public MemberLoveDataMap table() {
      return MemberLoveDataMap.table();
    }

    public MemberLoveDataMap table(int size) {
      return  MemberLoveDataMap.table(size);
    }

    public MemberLoveDataMap initTable() {
      return MemberLoveDataMap.table().init();
    }

    public MemberLoveDataMap initTable(int size) {
      return  MemberLoveDataMap.table(size).init();
    }

    public MemberLoveDataMap entity() {
      return MemberLoveDataMap.entity();
    }

    public MemberLoveDataMap entity(int size) {
      return  MemberLoveDataMap.entity(size);
    }
  }
}
