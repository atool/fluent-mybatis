package cn.org.atool.fluent.mybatis.generate;

import cn.org.atool.fluent.mybatis.generate.dm.HomeAddressDataMap;
import cn.org.atool.fluent.mybatis.generate.dm.NoAutoIdDataMap;
import cn.org.atool.fluent.mybatis.generate.dm.NoPrimaryDataMap;
import cn.org.atool.fluent.mybatis.generate.dm.StudentDataMap;
import cn.org.atool.fluent.mybatis.generate.dm.StudentScoreDataMap;
import cn.org.atool.fluent.mybatis.generate.mix.HomeAddressTableMix;
import cn.org.atool.fluent.mybatis.generate.mix.NoAutoIdTableMix;
import cn.org.atool.fluent.mybatis.generate.mix.NoPrimaryTableMix;
import cn.org.atool.fluent.mybatis.generate.mix.StudentScoreTableMix;
import cn.org.atool.fluent.mybatis.generate.mix.StudentTableMix;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import org.test4j.module.database.IDataSourceScript;
import org.test4j.module.spec.annotations.Mix;

/**
 * ATM: Application Table Manager
 *
 * @author Powered By Test4J
 */
public interface ATM {
  /**
   * 应用表名
   */
  interface Table {
    String noAutoId = "no_auto_id";

    String noPrimary = "no_primary";

    String student = "t_student";

    String studentScore = "student_score";

    String homeAddress = "home_address";
  }

  /**
   * table or entity data构造器
   */
  interface DataMap {
    NoAutoIdDataMap.Factory noAutoId = new NoAutoIdDataMap.Factory();

    NoPrimaryDataMap.Factory noPrimary = new NoPrimaryDataMap.Factory();

    StudentDataMap.Factory student = new StudentDataMap.Factory();

    StudentScoreDataMap.Factory studentScore = new StudentScoreDataMap.Factory();

    HomeAddressDataMap.Factory homeAddress = new HomeAddressDataMap.Factory();
  }

  /**
   * 应用表数据操作
   */
  @org.test4j.module.spec.annotations.Mixes
  class Mixes {
    @Mix
    public NoAutoIdTableMix noAutoIdTableMix;

    @Mix
    public NoPrimaryTableMix noPrimaryTableMix;

    @Mix
    public StudentTableMix studentTableMix;

    @Mix
    public StudentScoreTableMix studentScoreTableMix;

    @Mix
    public HomeAddressTableMix homeAddressTableMix;

    public void cleanAllTable() {
      this.noAutoIdTableMix.cleanNoAutoIdTable();
      this.noPrimaryTableMix.cleanNoPrimaryTable();
      this.studentTableMix.cleanStudentTable();
      this.studentScoreTableMix.cleanStudentScoreTable();
      this.homeAddressTableMix.cleanHomeAddressTable();
    }
  }

  /**
   * 应用数据库创建脚本构造
   */
  class Script implements IDataSourceScript {
    @Override
    public List<Class> getTableKlass() {
      return list(
      	NoAutoIdDataMap.class,
      	NoPrimaryDataMap.class,
      	StudentDataMap.class,
      	StudentScoreDataMap.class,
      	HomeAddressDataMap.class
      );
    }

    @Override
    public IndexList getIndexList() {
      return new IndexList();
    }
  }
}
