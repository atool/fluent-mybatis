package cn.org.atool.fluent.mybatis.generator.shared3.dm;

import java.util.Date;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * MemberDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("t_member")
@SuppressWarnings({"unused"})
public class MemberDataMap extends TableDataMap<MemberDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT UNSIGNED",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<MemberDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "age",
      type = "INT"
  )
  public final transient KeyValue<MemberDataMap> age = new KeyValue<>(this, "age", "age", supplier);

  @ColumnDef(
      value = "is_girl",
      type = "BIT",
      defaultValue = "0"
  )
  public final transient KeyValue<MemberDataMap> isGirl = new KeyValue<>(this, "is_girl", "isGirl", supplier);

  @ColumnDef(
      value = "school",
      type = "VARCHAR(20)"
  )
  public final transient KeyValue<MemberDataMap> school = new KeyValue<>(this, "school", "school", supplier);

  @ColumnDef(
      value = "user_name",
      type = "VARCHAR(45)"
  )
  public final transient KeyValue<MemberDataMap> userName = new KeyValue<>(this, "user_name", "userName", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "DATETIME"
  )
  public final transient KeyValue<MemberDataMap> gmtCreated = new KeyValue<>(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "DATETIME"
  )
  public final transient KeyValue<MemberDataMap> gmtModified = new KeyValue<>(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "BIT",
      defaultValue = "0"
  )
  public final transient KeyValue<MemberDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  MemberDataMap(boolean isTable) {
    super("t_member", isTable);
  }

  MemberDataMap(boolean isTable, int size) {
    super("t_member", isTable, size);
  }

  /**
   * 创建MemberDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public MemberDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
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

  public static class Factory extends BaseFactory<MemberDataMap> {
    public Factory() {
      super(MemberDataMap.class);
    }
  }
}
