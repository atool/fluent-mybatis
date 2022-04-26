package cn.org.atool.fluent.mybatis.generator.shared5.dm;

import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * IdcardDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("idcard")
@SuppressWarnings({"unused"})
public class IdcardDataMap extends TableDataMap<IdcardDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT UNSIGNED",
      primary = true,
      notNull = true
  )
  public final transient KeyValue<IdcardDataMap> id = new KeyValue<>(this, "id", "id", supplier);

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

  @ColumnDef(
      value = "is_deleted",
      type = "BIGINT",
      defaultValue = "0"
  )
  public final transient KeyValue<IdcardDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  IdcardDataMap(boolean isTable) {
    super("idcard", isTable);
  }

  IdcardDataMap(boolean isTable, int size) {
    super("idcard", isTable, size);
  }

  /**
   * 创建IdcardDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public IdcardDataMap init() {
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

  public static class Factory extends BaseFactory<IdcardDataMap> {
    public Factory() {
      super(IdcardDataMap.class);
    }
  }
}
