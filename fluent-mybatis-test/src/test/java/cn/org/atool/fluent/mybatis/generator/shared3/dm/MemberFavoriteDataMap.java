package cn.org.atool.fluent.mybatis.generator.shared3.dm;

import java.util.Date;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * MemberFavoriteDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("t_member_favorite")
@SuppressWarnings({"unused"})
public class MemberFavoriteDataMap extends TableDataMap<MemberFavoriteDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT UNSIGNED",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<MemberFavoriteDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "favorite",
      type = "VARCHAR(45)"
  )
  public final transient KeyValue<MemberFavoriteDataMap> favorite = new KeyValue<>(this, "favorite", "favorite", supplier);

  @ColumnDef(
      value = "member_id",
      type = "BIGINT",
      notNull = true
  )
  public final transient KeyValue<MemberFavoriteDataMap> memberId = new KeyValue<>(this, "member_id", "memberId", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "DATETIME"
  )
  public final transient KeyValue<MemberFavoriteDataMap> gmtCreated = new KeyValue<>(this, "gmt_created", "gmtCreated", supplier);

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

  MemberFavoriteDataMap(boolean isTable) {
    super("t_member_favorite", isTable);
  }

  MemberFavoriteDataMap(boolean isTable, int size) {
    super("t_member_favorite", isTable, size);
  }

  /**
   * 创建MemberFavoriteDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public MemberFavoriteDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
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

  public static class Factory extends BaseFactory<MemberFavoriteDataMap> {
    public Factory() {
      super(MemberFavoriteDataMap.class);
    }
  }
}
