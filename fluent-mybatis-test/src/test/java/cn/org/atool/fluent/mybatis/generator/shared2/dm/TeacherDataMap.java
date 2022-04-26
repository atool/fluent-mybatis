package cn.org.atool.fluent.mybatis.generator.shared2.dm;

import java.util.Date;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * TeacherDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("teacher")
@SuppressWarnings({"unused"})
public class TeacherDataMap extends TableDataMap<TeacherDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<TeacherDataMap> id = new KeyValue<>(this, "id", "id", supplier);

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

  TeacherDataMap(boolean isTable) {
    super("teacher", isTable);
  }

  TeacherDataMap(boolean isTable, int size) {
    super("teacher", isTable, size);
  }

  /**
   * 创建TeacherDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public TeacherDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
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

  public static class Factory extends BaseFactory<TeacherDataMap> {
    public Factory() {
      super(TeacherDataMap.class);
    }
  }
}
