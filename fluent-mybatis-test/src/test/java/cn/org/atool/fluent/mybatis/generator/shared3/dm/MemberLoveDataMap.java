package cn.org.atool.fluent.mybatis.generator.shared3.dm;

import java.util.Date;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * MemberLoveDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("t_member_love")
@SuppressWarnings({"unused"})
public class MemberLoveDataMap extends TableDataMap<MemberLoveDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT UNSIGNED",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<MemberLoveDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "boy_id",
      type = "BIGINT",
      notNull = true
  )
  public final transient KeyValue<MemberLoveDataMap> boyId = new KeyValue<>(this, "boy_id", "boyId", supplier);

  @ColumnDef(
      value = "girl_id",
      type = "BIGINT",
      notNull = true
  )
  public final transient KeyValue<MemberLoveDataMap> girlId = new KeyValue<>(this, "girl_id", "girlId", supplier);

  @ColumnDef(
      value = "status",
      type = "VARCHAR(45)"
  )
  public final transient KeyValue<MemberLoveDataMap> status = new KeyValue<>(this, "status", "status", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "DATETIME"
  )
  public final transient KeyValue<MemberLoveDataMap> gmtCreated = new KeyValue<>(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "DATETIME"
  )
  public final transient KeyValue<MemberLoveDataMap> gmtModified = new KeyValue<>(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "TINYINT",
      defaultValue = "0"
  )
  public final transient KeyValue<MemberLoveDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  MemberLoveDataMap(boolean isTable) {
    super("t_member_love", isTable);
  }

  MemberLoveDataMap(boolean isTable, int size) {
    super("t_member_love", isTable, size);
  }

  /**
   * 创建MemberLoveDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public MemberLoveDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
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

  public static class Factory extends BaseFactory<MemberLoveDataMap> {
    public Factory() {
      super(MemberLoveDataMap.class);
    }
  }
}
