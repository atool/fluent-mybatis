package cn.org.atool.fluent.mybatis.tutorial.datamap;

import cn.org.atool.fluent.mybatis.tutorial.datamap.entity.*;

/**
 * Entity Data Map
 */
public interface EM {

    ReceivingAddressEntityMap.Factory receivingAddress = new ReceivingAddressEntityMap.Factory();

    UserEntityMap.Factory user = new UserEntityMap.Factory();
}