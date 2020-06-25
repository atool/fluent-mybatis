package cn.org.atool.fluent.mybatis.tutorial.datamap;

import cn.org.atool.fluent.mybatis.tutorial.datamap.table.*;

/**
 * Table Data Map
 */
public interface TM {

    ReceivingAddressTableMap.Factory receiving_address = new ReceivingAddressTableMap.Factory();

    UserTableMap.Factory user = new UserTableMap.Factory();
}