package cn.org.atool.fluent.mybatis.generate.dao.impl;

import cn.org.atool.fluent.mybatis.generate.entity.dao.UserBaseDao;
import cn.org.atool.fluent.mybatis.generate.dao.intf.UserDao;
import cn.org.atool.fluent.mybatis.generate.entity.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.generate.entity.wrapper.UserUpdate;
import org.springframework.stereotype.Repository;

/**
 * @author generate code
 * @ClassName UserDaoImpl
 * @Description UserEntity数据操作实现类
 */
@Repository
public class UserDaoImpl extends UserBaseDao implements UserDao {
    @Override
    public int noOverWrite() {
        UserQuery query = super.defaultQuery();
        UserUpdate updater = super.defaultUpdater();
        return 10;
    }
}