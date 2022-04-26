package cn.org.atool.fluent.mybatis.generator.shared2.dm;

import java.util.Date;
import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.database.datagen.BaseFactory;
import org.test4j.module.database.datagen.TableDataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * StudentTeacherRelationDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("student_teacher_relation")
@SuppressWarnings({"unused"})
public class StudentTeacherRelationDataMap extends TableDataMap<StudentTeacherRelationDataMap> {
  @ColumnDef(
      value = "id",
      type = "BIGINT",
      primary = true,
      autoIncrease = true,
      notNull = true
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> id = new KeyValue<>(this, "id", "id", supplier);

  @ColumnDef(
      value = "env",
      type = "VARCHAR(10)"
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> env = new KeyValue<>(this, "env", "env", supplier);

  @ColumnDef(
      value = "student_id",
      type = "BIGINT",
      notNull = true
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> studentId = new KeyValue<>(this, "student_id", "studentId", supplier);

  @ColumnDef(
      value = "teacher_id",
      type = "BIGINT",
      notNull = true
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> teacherId = new KeyValue<>(this, "teacher_id", "teacherId", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "DATETIME"
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> gmtCreated = new KeyValue<>(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "DATETIME"
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> gmtModified = new KeyValue<>(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "TINYINT",
      defaultValue = "0"
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> isDeleted = new KeyValue<>(this, "is_deleted", "isDeleted", supplier);

  StudentTeacherRelationDataMap(boolean isTable) {
    super("student_teacher_relation", isTable);
  }

  StudentTeacherRelationDataMap(boolean isTable, int size) {
    super("student_teacher_relation", isTable, size);
  }

  /**
   * 创建StudentTeacherRelationDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  @Override
  public StudentTeacherRelationDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public static StudentTeacherRelationDataMap table() {
    return new StudentTeacherRelationDataMap(true, 1);
  }

  public static StudentTeacherRelationDataMap table(int size) {
    return new StudentTeacherRelationDataMap(true, size);
  }

  public static StudentTeacherRelationDataMap entity() {
    return new StudentTeacherRelationDataMap(false, 1);
  }

  public static StudentTeacherRelationDataMap entity(int size) {
    return new StudentTeacherRelationDataMap(false, size);
  }

  public static class Factory extends BaseFactory<StudentTeacherRelationDataMap> {
    public Factory() {
      super(StudentTeacherRelationDataMap.class);
    }
  }
}
