package cn.org.atool.fluent.mybatis.tutorial.dao.base;

import cn.org.atool.fluent.mybatis.base.impl.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.tutorial.entity.UserEntity;
import cn.org.atool.fluent.mybatis.tutorial.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.tutorial.helper.UserMapping;
import cn.org.atool.fluent.mybatis.tutorial.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.tutorial.wrapper.UserUpdate;
import org.springframework.beans.factory.annotation.Autowired;


/**
* UserEntity数据库操作服务类
 *
 * @author generate code
*/
public abstract class UserBaseDao extends BaseDaoImpl<UserEntity, UserQuery, UserUpdate>
        implements UserMapping {

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