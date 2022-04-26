package cn.org.atool.fluent.mybatis.generator.shared2.dm;

import java.util.Date;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * StudentScoreDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("student_score")
@SuppressWarnings({"unused"})
public class StudentScoreDataMap extends TableDataMap<StudentScoreDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<StudentScoreDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "env",
      type = "VARCHAR(10)"
  )
  public final transient KeyValue<StudentScoreDataMap> env = new KeyValue<>(this, "env", "env", supplier);

  @ColumnDef(
      value = "gender",
      type = "TINYINT",
      notNull = true,
      defaultValue = "0"
  )
  public final transient KeyValue<StudentScoreDataMap> gender = new KeyValue<>(this, "gender", "gender", supplier);

  @ColumnDef(
      value = "school_term",
      type = "INT"
  )
  public final transient KeyValue<StudentScoreDataMap> schoolTerm = new KeyValue<>(this, "school_term", "schoolTerm", supplier);

  @ColumnDef(
      value = "score",
      type = "INT"
  )
  public final transient KeyValue<StudentScoreDataMap> score = new KeyValue<>(this, "score", "score", supplier);

  @ColumnDef(
      value = "student_id",
      type = "BIGINT",
      notNull = true
  )
  public final transient KeyValue<StudentScoreDataMap> studentId = new KeyValue<>(this, "student_id", "studentId", supplier);

  @ColumnDef(
      value = "subject",
      type = "VARCHAR(30)"
  )
  public final transient KeyValue<StudentScoreDataMap> subject = new KeyValue<>(this, "subject", "subject", supplier);

  @ColumnDef(
      value = "tenant",
      type = "BIGINT",
      notNull = true,
      defaultValue = "0"
  )
  public final transient KeyValue<StudentScoreDataMap> tenant = new KeyValue<>(this, "tenant", "tenant", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "DATETIME",
      notNull = true
  )
  public final transient KeyValue<StudentScoreDataMap> gmtCreated = new KeyValue<>(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "DATETIME",
      notNull = true
  )
  public final transient KeyValue<StudentScoreDataMap> gmtModified = new KeyValue<>(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "TINYINT",
      notNull = true,
      defaultValue = "0"
  )
  public final transient KeyValue<StudentScoreDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  StudentScoreDataMap(boolean isTable) {
    super("student_score", isTable);
  }

  StudentScoreDataMap(boolean isTable, int size) {
    super("student_score", isTable, size);
  }

  /**
   * 创建StudentScoreDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public StudentScoreDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public static StudentScoreDataMap table() {
    return new StudentScoreDataMap(true, 1);
  }

  public static StudentScoreDataMap table(int size) {
    return new StudentScoreDataMap(true, size);
  }

  public static StudentScoreDataMap entity() {
    return new StudentScoreDataMap(false, 1);
  }

  public static StudentScoreDataMap entity(int size) {
    return new StudentScoreDataMap(false, size);
  }

  public static class Factory extends BaseFactory<StudentScoreDataMap> {
    public Factory() {
      super(StudentScoreDataMap.class);
    }
  }
}
