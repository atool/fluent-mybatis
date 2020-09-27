package cn.org.atool.fluent.mybatis.generate.datamap;

import cn.org.atool.fluent.mybatis.generate.datamap.table.AddressTableMap;
import cn.org.atool.fluent.mybatis.generate.datamap.table.NoAutoIdTableMap;
import cn.org.atool.fluent.mybatis.generate.datamap.table.NoPrimaryTableMap;
import cn.org.atool.fluent.mybatis.generate.datamap.table.UserTableMap;

/**
 * Table Data Map
 */
public interface TM {

    AddressTableMap.Factory address = new AddressTableMap.Factory();

    UserTableMap.Factory user = new UserTableMap.Factory();

    NoPrimaryTableMap.Factory no_primary = new NoPrimaryTableMap.Factory();

    NoAutoIdTableMap.Factory no_auto_id = new NoAutoIdTableMap.Factory();
}