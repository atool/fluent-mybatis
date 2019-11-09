package cn.org.atool.fluent.mybatis.demo.generate.datamap;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.table.*;

/**
 * Table Data Map
 */
public interface TM {

    AddressTableMap.Factory address = new AddressTableMap.Factory();

    UserTableMap.Factory user = new UserTableMap.Factory();
}
