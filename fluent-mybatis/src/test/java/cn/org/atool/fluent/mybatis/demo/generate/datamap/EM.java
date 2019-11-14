package cn.org.atool.fluent.mybatis.demo.generate.datamap;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.entity.*;

/**
 * Entity Data Map
 */
public interface EM {

    AddressEntityMap.Factory address = new AddressEntityMap.Factory();

    UserEntityMap.Factory user = new UserEntityMap.Factory();

    NoAutoIdEntityMap.Factory noAutoId = new NoAutoIdEntityMap.Factory();

    NoPrimaryEntityMap.Factory noPrimary = new NoPrimaryEntityMap.Factory();
}
