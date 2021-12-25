package cn.org.atool.fluent.mybatis.generator;

import cn.org.atool.fluent.mybatis.generator.shared1.dm.NoAutoIdDataMap;
import cn.org.atool.fluent.mybatis.generator.shared1.dm.NoPrimaryDataMap;
import cn.org.atool.fluent.mybatis.generator.shared1.mix.NoAutoIdTableMix;
import cn.org.atool.fluent.mybatis.generator.shared1.mix.NoPrimaryTableMix;
import cn.org.atool.fluent.mybatis.generator.shared2.dm.HomeAddressDataMap;
import cn.org.atool.fluent.mybatis.generator.shared2.dm.StudentDataMap;
import cn.org.atool.fluent.mybatis.generator.shared2.dm.StudentScoreDataMap;
import cn.org.atool.fluent.mybatis.generator.shared2.dm.StudentTeacherRelationDataMap;
import cn.org.atool.fluent.mybatis.generator.shared2.dm.TeacherDataMap;
import cn.org.atool.fluent.mybatis.generator.shared2.mix.HomeAddressTableMix;
import cn.org.atool.fluent.mybatis.generator.shared2.mix.StudentScoreTableMix;
import cn.org.atool.fluent.mybatis.generator.shared2.mix.StudentTableMix;
import cn.org.atool.fluent.mybatis.generator.shared2.mix.StudentTeacherRelationTableMix;
import cn.org.atool.fluent.mybatis.generator.shared2.mix.TeacherTableMix;
import cn.org.atool.fluent.mybatis.generator.shared3.dm.MemberDataMap;
import cn.org.atool.fluent.mybatis.generator.shared3.dm.MemberFavoriteDataMap;
import cn.org.atool.fluent.mybatis.generator.shared3.dm.MemberLoveDataMap;
import cn.org.atool.fluent.mybatis.generator.shared3.mix.MemberFavoriteTableMix;
import cn.org.atool.fluent.mybatis.generator.shared3.mix.MemberLoveTableMix;
import cn.org.atool.fluent.mybatis.generator.shared3.mix.MemberTableMix;
import cn.org.atool.fluent.mybatis.generator.shared4.dm.BlobValueDataMap;
import cn.org.atool.fluent.mybatis.generator.shared4.dm.MyEnumTypeDataMap;
import cn.org.atool.fluent.mybatis.generator.shared4.mix.BlobValueTableMix;
import cn.org.atool.fluent.mybatis.generator.shared4.mix.MyEnumTypeTableMix;
import cn.org.atool.fluent.mybatis.generator.shared5.dm.IdcardDataMap;
import cn.org.atool.fluent.mybatis.generator.shared5.mix.IdcardTableMix;
import java.util.List;
import org.test4j.module.database.IDataSourceScript;
import org.test4j.module.spec.internal.MixProxy;

/**
 * ATM: Application Table Manager
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes"})
public interface ATM {
  DataMap dataMap = new DataMap();

  Table table = new Table();

  Mixes mixes = new Mixes();

  /**
   * 应用表名
   */
  class Table {
    public final String noAutoId = "no_auto_id";

    public final String noPrimary = "no_primary";

    public final String studentTeacherRelation = "student_teacher_relation";

    public final String studentScore = "student_score";

    public final String teacher = "teacher";

    public final String homeAddress = "home_address";

    public final String student = "student";

    public final String memberFavorite = "t_member_favorite";

    public final String memberLove = "t_member_love";

    public final String member = "t_member";

    public final String blobValue = "blob_value";

    public final String myEnumType = "my_enum_type";

    public final String idcard = "idcard";
  }

  /**
   * table or entity data构造器
   */
  class DataMap {
    public final NoAutoIdDataMap.Factory noAutoId = new NoAutoIdDataMap.Factory();

    public final NoPrimaryDataMap.Factory noPrimary = new NoPrimaryDataMap.Factory();

    public final StudentTeacherRelationDataMap.Factory studentTeacherRelation = new StudentTeacherRelationDataMap.Factory();

    public final StudentScoreDataMap.Factory studentScore = new StudentScoreDataMap.Factory();

    public final TeacherDataMap.Factory teacher = new TeacherDataMap.Factory();

    public final HomeAddressDataMap.Factory homeAddress = new HomeAddressDataMap.Factory();

    public final StudentDataMap.Factory student = new StudentDataMap.Factory();

    public final MemberFavoriteDataMap.Factory memberFavorite = new MemberFavoriteDataMap.Factory();

    public final MemberLoveDataMap.Factory memberLove = new MemberLoveDataMap.Factory();

    public final MemberDataMap.Factory member = new MemberDataMap.Factory();

    public final BlobValueDataMap.Factory blobValue = new BlobValueDataMap.Factory();

    public final MyEnumTypeDataMap.Factory myEnumType = new MyEnumTypeDataMap.Factory();

    public final IdcardDataMap.Factory idcard = new IdcardDataMap.Factory();
  }

  /**
   * 应用表数据操作
   */
  class Mixes {
    public final NoAutoIdTableMix noAutoIdTableMix = MixProxy.proxy(NoAutoIdTableMix.class);

    public final NoPrimaryTableMix noPrimaryTableMix = MixProxy.proxy(NoPrimaryTableMix.class);

    public final StudentTeacherRelationTableMix studentTeacherRelationTableMix = MixProxy.proxy(StudentTeacherRelationTableMix.class);

    public final StudentScoreTableMix studentScoreTableMix = MixProxy.proxy(StudentScoreTableMix.class);

    public final TeacherTableMix teacherTableMix = MixProxy.proxy(TeacherTableMix.class);

    public final HomeAddressTableMix homeAddressTableMix = MixProxy.proxy(HomeAddressTableMix.class);

    public final StudentTableMix studentTableMix = MixProxy.proxy(StudentTableMix.class);

    public final MemberFavoriteTableMix memberFavoriteTableMix = MixProxy.proxy(MemberFavoriteTableMix.class);

    public final MemberLoveTableMix memberLoveTableMix = MixProxy.proxy(MemberLoveTableMix.class);

    public final MemberTableMix memberTableMix = MixProxy.proxy(MemberTableMix.class);

    public final BlobValueTableMix blobValueTableMix = MixProxy.proxy(BlobValueTableMix.class);

    public final MyEnumTypeTableMix myEnumTypeTableMix = MixProxy.proxy(MyEnumTypeTableMix.class);

    public final IdcardTableMix idcardTableMix = MixProxy.proxy(IdcardTableMix.class);

    public void cleanAllTable() {
      this.noAutoIdTableMix.cleanNoAutoIdTable();
      this.noPrimaryTableMix.cleanNoPrimaryTable();
      this.studentTeacherRelationTableMix.cleanStudentTeacherRelationTable();
      this.studentScoreTableMix.cleanStudentScoreTable();
      this.teacherTableMix.cleanTeacherTable();
      this.homeAddressTableMix.cleanHomeAddressTable();
      this.studentTableMix.cleanStudentTable();
      this.memberFavoriteTableMix.cleanMemberFavoriteTable();
      this.memberLoveTableMix.cleanMemberLoveTable();
      this.memberTableMix.cleanMemberTable();
      this.blobValueTableMix.cleanBlobValueTable();
      this.myEnumTypeTableMix.cleanMyEnumTypeTable();
      this.idcardTableMix.cleanIdcardTable();
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
      	StudentTeacherRelationDataMap.class,
      	StudentScoreDataMap.class,
      	TeacherDataMap.class,
      	HomeAddressDataMap.class,
      	StudentDataMap.class,
      	MemberFavoriteDataMap.class,
      	MemberLoveDataMap.class,
      	MemberDataMap.class,
      	BlobValueDataMap.class,
      	MyEnumTypeDataMap.class,
      	IdcardDataMap.class
      );
    }

    @Override
    public IndexList getIndexList() {
      return new IndexList();
    }
  }
}
