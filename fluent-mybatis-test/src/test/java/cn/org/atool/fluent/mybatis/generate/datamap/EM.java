package cn.org.atool.fluent.mybatis.generate.datamap;

import cn.org.atool.fluent.mybatis.generate.datamap.entity.AddressEntityMap;
import cn.org.atool.fluent.mybatis.generate.datamap.entity.NoAutoIdEntityMap;
import cn.org.atool.fluent.mybatis.generate.datamap.entity.NoPrimaryEntityMap;
import cn.org.atool.fluent.mybatis.generate.datamap.entity.UserEntityMap;

/**
 * Entity Data Map
 */
public interface EM {

    AddressEntityMap.Factory address = new AddressEntityMap.Factory();

    UserEntityMap.Factory user = new UserEntityMap.Factory();

    NoPrimaryEntityMap.Factory noPrimary = new NoPrimaryEntityMap.Factory();

    NoAutoIdEntityMap.Factory noAutoId = new NoAutoIdEntityMap.Factory();
}