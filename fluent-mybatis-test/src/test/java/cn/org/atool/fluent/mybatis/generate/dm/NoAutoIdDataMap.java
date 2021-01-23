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
 * NoAutoIdDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("no_auto_id")
public class NoAutoIdDataMap extends DataMap<NoAutoIdDataMap> {
  private boolean isTable;

  private Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "id",
      type = "varchar(50)",
      primary = true
  )
  public final transient KeyValue<NoAutoIdDataMap> id = new KeyValue(this, "id", "id", supplier);

  @ColumnDef(
      value = "column_1",
      type = "varchar(20)"
  )
  public final transient KeyValue<NoAutoIdDataMap> column1 = new KeyValue(this, "column_1", "column1", supplier);

  NoAutoIdDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  NoAutoIdDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建NoAutoIdDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public NoAutoIdDataMap init() {
    return this;
  }

  public NoAutoIdDataMap with(Consumer<NoAutoIdDataMap> init) {
    init.accept(this);
    return this;
  }

  public static NoAutoIdDataMap table() {
    return new NoAutoIdDataMap(true, 1);
  }

  public static NoAutoIdDataMap table(int size) {
    return new NoAutoIdDataMap(true, size);
  }

  public static NoAutoIdDataMap entity() {
    return new NoAutoIdDataMap(false, 1);
  }

  public static NoAutoIdDataMap entity(int size) {
    return new NoAutoIdDataMap(false, size);
  }

  /**
   * DataMap数据和表[no_auto_id]数据比较
   */
  public NoAutoIdDataMap eqTable(EqMode... modes) {
    IDatabase.db.table("no_auto_id").query().eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[no_auto_id]数据比较
   */
  public NoAutoIdDataMap eqQuery(String query, EqMode... modes) {
    IDatabase.db.table("no_auto_id").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[no_auto_id]数据比较
   */
  public NoAutoIdDataMap eqQuery(IDataMap query, EqMode... modes) {
    IDatabase.db.table("no_auto_id").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * 清空[no_auto_id]表数据
   */
  public NoAutoIdDataMap clean() {
    IDatabase.db.cleanTable("no_auto_id");
    return this;
  }

  /**
   * 插入[no_auto_id]表数据
   */
  public NoAutoIdDataMap insert() {
    IDatabase.db.table("no_auto_id").insert(this);
    return this;
  }

  /**
   * 先清空, 再插入[no_auto_id]表数据
   */
  public NoAutoIdDataMap cleanAndInsert() {
    return this.clean().insert();
  }

  public static class Factory {
    public NoAutoIdDataMap table() {
      return NoAutoIdDataMap.table();
    }

    public NoAutoIdDataMap table(int size) {
      return  NoAutoIdDataMap.table(size);
    }

    public NoAutoIdDataMap initTable() {
      return NoAutoIdDataMap.table().init();
    }

    public NoAutoIdDataMap initTable(int size) {
      return  NoAutoIdDataMap.table(size).init();
    }

    public NoAutoIdDataMap entity() {
      return NoAutoIdDataMap.entity();
    }

    public NoAutoIdDataMap entity(int size) {
      return  NoAutoIdDataMap.entity(size);
    }
  }
}
