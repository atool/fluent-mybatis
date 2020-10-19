package cn.org.atool.fluent.mybatis.generate;

import cn.org.atool.fluent.mybatis.generate.dm.AddressDataMap;
import cn.org.atool.fluent.mybatis.generate.dm.NoAutoIdDataMap;
import cn.org.atool.fluent.mybatis.generate.dm.NoPrimaryDataMap;
import cn.org.atool.fluent.mybatis.generate.dm.UserDataMap;
import cn.org.atool.fluent.mybatis.generate.mix.AddressTableMix;
import cn.org.atool.fluent.mybatis.generate.mix.NoAutoIdTableMix;
import cn.org.atool.fluent.mybatis.generate.mix.NoPrimaryTableMix;
import cn.org.atool.fluent.mybatis.generate.mix.UserTableMix;
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
    String address = "address";

    String noPrimary = "no_primary";

    String user = "t_user";

    String noAutoId = "no_auto_id";
  }

  /**
   * 应用表数据构造器
   */
  interface DataMap {
    AddressDataMap.Factory address = new AddressDataMap.Factory();

    NoPrimaryDataMap.Factory noPrimary = new NoPrimaryDataMap.Factory();

    UserDataMap.Factory user = new UserDataMap.Factory();

    NoAutoIdDataMap.Factory noAutoId = new NoAutoIdDataMap.Factory();
  }

  /**
   * 应用表数据操作
   */
  @org.test4j.module.spec.annotations.Mixes
  class Mixes {
    @Mix
    public AddressTableMix addressTableMix;

    @Mix
    public NoPrimaryTableMix noPrimaryTableMix;

    @Mix
    public UserTableMix userTableMix;

    @Mix
    public NoAutoIdTableMix noAutoIdTableMix;

    public void cleanAllTable() {
      this.addressTableMix.cleanAddressTable();
      this.noPrimaryTableMix.cleanNoPrimaryTable();
      this.userTableMix.cleanUserTable();
      this.noAutoIdTableMix.cleanNoAutoIdTable();
    }
  }

  /**
   * 应用数据库创建脚本构造
   */
  class Script implements IDataSourceScript {
    @Override
    public List<Class> getTableKlass() {
      return list(
      	AddressDataMap.class,
      	NoPrimaryDataMap.class,
      	UserDataMap.class,
      	NoAutoIdDataMap.class
      );
    }

    @Override
    public IndexList getIndexList() {
      return new IndexList();
    }
  }
}
