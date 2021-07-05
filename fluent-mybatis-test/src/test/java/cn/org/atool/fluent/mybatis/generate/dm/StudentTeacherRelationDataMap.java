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
 * StudentTeacherRelationDataMap: 表(实体)数据对比(插入)构造器
 *
 * @author Powered By Test4J
 */
@ScriptTable("student_teacher_relation")
public class StudentTeacherRelationDataMap extends DataMap<StudentTeacherRelationDataMap> {
  private boolean isTable;

  private Supplier<Boolean> supplier = () -> this.isTable;

  @ColumnDef(
      value = "id",
      type = "bigint(20)",
      primary = true,
      autoIncrease = true
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> id = new KeyValue(this, "id", "id", supplier);

  @ColumnDef(
      value = "gmt_created",
      type = "datetime"
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> gmtCreated = new KeyValue(this, "gmt_created", "gmtCreated", supplier);

  @ColumnDef(
      value = "gmt_modified",
      type = "datetime"
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> gmtModified = new KeyValue(this, "gmt_modified", "gmtModified", supplier);

  @ColumnDef(
      value = "is_deleted",
      type = "tinyint(2)"
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> isDeleted = new KeyValue(this, "is_deleted", "isDeleted", supplier);

  @ColumnDef(
      value = "env",
      type = "varchar(10)"
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> env = new KeyValue(this, "env", "env", supplier);

  @ColumnDef(
      value = "student_id",
      type = "bigint(20)"
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> studentId = new KeyValue(this, "student_id", "studentId", supplier);

  @ColumnDef(
      value = "teacher_id",
      type = "bigint(20)"
  )
  public final transient KeyValue<StudentTeacherRelationDataMap> teacherId = new KeyValue(this, "teacher_id", "teacherId", supplier);

  StudentTeacherRelationDataMap(boolean isTable) {
    super();
    this.isTable = isTable;
  }

  StudentTeacherRelationDataMap(boolean isTable, int size) {
    super(size);
    this.isTable = isTable;
  }

  /**
   * 创建StudentTeacherRelationDataMap
   * 初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
   */
  public StudentTeacherRelationDataMap init() {
    this.id.autoIncrease();
    this.gmtCreated.values(new Date());
    this.gmtModified.values(new Date());
    this.isDeleted.values(false);
    return this;
  }

  public StudentTeacherRelationDataMap with(Consumer<StudentTeacherRelationDataMap> init) {
    init.accept(this);
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

  /**
   * DataMap数据和表[student_teacher_relation]数据比较
   */
  public StudentTeacherRelationDataMap eqTable(EqMode... modes) {
    IDatabase.db.table("student_teacher_relation").query().eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[student_teacher_relation]数据比较
   */
  public StudentTeacherRelationDataMap eqQuery(String query, EqMode... modes) {
    IDatabase.db.table("student_teacher_relation").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * DataMap数据和表[student_teacher_relation]数据比较
   */
  public StudentTeacherRelationDataMap eqQuery(IDataMap query, EqMode... modes) {
    IDatabase.db.table("student_teacher_relation").queryWhere(query).eqDataMap(this, modes);
    return this;
  }

  /**
   * 清空[student_teacher_relation]表数据
   */
  public StudentTeacherRelationDataMap clean() {
    IDatabase.db.cleanTable("student_teacher_relation");
    return this;
  }

  /**
   * 插入[student_teacher_relation]表数据
   */
  public StudentTeacherRelationDataMap insert() {
    IDatabase.db.table("student_teacher_relation").insert(this);
    return this;
  }

  /**
   * 先清空, 再插入[student_teacher_relation]表数据
   */
  public StudentTeacherRelationDataMap cleanAndInsert() {
    return this.clean().insert();
  }

  public static class Factory {
    public StudentTeacherRelationDataMap table() {
      return StudentTeacherRelationDataMap.table();
    }

    public StudentTeacherRelationDataMap table(int size) {
      return  StudentTeacherRelationDataMap.table(size);
    }

    public StudentTeacherRelationDataMap initTable() {
      return StudentTeacherRelationDataMap.table().init();
    }

    public StudentTeacherRelationDataMap initTable(int size) {
      return  StudentTeacherRelationDataMap.table(size).init();
    }

    public StudentTeacherRelationDataMap entity() {
      return StudentTeacherRelationDataMap.entity();
    }

    public StudentTeacherRelationDataMap entity(int size) {
      return  StudentTeacherRelationDataMap.entity(size);
    }
  }
}
