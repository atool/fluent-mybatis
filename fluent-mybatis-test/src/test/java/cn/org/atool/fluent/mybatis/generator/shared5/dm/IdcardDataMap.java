package cn.org.atool.fluent.mybatis.generator.shared5.dm;

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
 * IdcardDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("idcard")
@SuppressWarnings({"unused", "rawtypes"})
public class IdcardDataMap extends DataMap<IdcardDataMap> {
  private boolean isTable;

  private final Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "id",
      type = "BIGINT UNSIGNED",
      primary = true,
      notNull = true
  )
  public final transient KeyValue<IdcardDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "BIGINT",
      defaultValue = "0"
  )
  public final transient KeyValue<IdcardDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  @ColumnDef(
      value = "code",
      type = "VARCHAR(18)"
  )
  public final transient KeyValue<IdcardDataMap> code = new KeyValue<>(this, "code", "code", supplier);

  @ColumnDef(
      value = "version",
      type = "BIGINT UNSIGNED",
      notNull = true,
      defaultValue = "0"
  )
  public final transient KeyValue<IdcardDataMap> version = new KeyValue<>(this, "version", "version", supplier);

  IdcardDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  IdcardDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建IdcardDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public IdcardDataMap init() {
    this.id.autoIncrease();
    return this;
  }

  public IdcardDataMap with(Consumer<IdcardDataMap> init) {
    init.accept(this);
    return this;
  }

  public static IdcardDataMap table() {
    return new IdcardDataMap(true, 1);
  }

  public static IdcardDataMap table(int size) {
    return new IdcardDataMap(true, size);
  }

  public static IdcardDataMap entity() {
    return new IdcardDataMap(false, 1);
  }

  public static IdcardDataMap entity(int size) {
    return new IdcardDataMap(false, size);
  }

  /**
   * DataMap数据和表[idcard]数据比较
   */
  public IdcardDataMap eqTable(EqMode... modes) {
    IDatabase.db.table("idcard").query().eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[idcard]数据比较
   */
  public IdcardDataMap eqQuery(String query, EqMode... modes) {
    IDatabase.db.table("idcard").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[idcard]数据比较
   */
  public IdcardDataMap eqQuery(IDataMap query, EqMode... modes) {
    IDatabase.db.table("idcard").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * 清空[idcard]表数据
   */
  public IdcardDataMap clean() {
    IDatabase.db.cleanTable("idcard");
    return this;
  }

  /**
   * 插入[idcard]表数据
   */
  public IdcardDataMap insert() {
    IDatabase.db.table("idcard").insert(this);
    return this;
  }

  /**
   * 先清空, 再插入[idcard]表数据
   */
  public IdcardDataMap cleanAndInsert() {
    return this.clean().insert();
  }

  public static class Factory {
    public IdcardDataMap table() {
      return IdcardDataMap.table();
    }

    public IdcardDataMap table(int size) {
      return  IdcardDataMap.table(size);
    }

    public IdcardDataMap initTable() {
      return IdcardDataMap.table().init();
    }

    public IdcardDataMap initTable(int size) {
      return  IdcardDataMap.table(size).init();
    }

    public IdcardDataMap entity() {
      return IdcardDataMap.entity();
    }

    public IdcardDataMap entity(int size) {
      return  IdcardDataMap.entity(size);
    }
  }
}