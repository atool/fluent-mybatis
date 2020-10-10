package cn.org.atool.fluent.mybatis.generate;

import cn.org.atool.fluent.mybatis.generate.dm.*;

/**
 * Table Data Map
 */
public interface DM {

    AddressDataMap.Factory address = new AddressDataMap.Factory();

    NoPrimaryDataMap.Factory noPrimary = new NoPrimaryDataMap.Factory();

    UserDataMap.Factory user = new UserDataMap.Factory();

    NoAutoIdDataMap.Factory noAutoId = new NoAutoIdDataMap.Factory();
}
