package cn.org.atool.fluent.mybatis.generate.dao.base;

import cn.org.atool.fluent.mybatis.base.impl.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.helper.UserMapping;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;

/**
* UserEntity数据库操作服务类
 *
 * @author generate code
*/
public abstract class UserBaseDao extends BaseDaoImpl<UserEntity>
        implements UserMapping, MyCustomerInterface<UserEntity> {

    @Autowired
    protected UserMapper mapper;

    @Override
    public UserMapper mapper() {
        return mapper;
    }

    @Override
    public UserQuery query(){
        return new UserQuery();
    }

    @Override
    public UserUpdate updater(){
        return new UserUpdate();
    }

    @Override
    public String findPkColumn() {
        return UserMapping.id.column;
    }
}