package cn.org.atool.mybatis.fluent.demo.dao.base;

import cn.org.atool.mybatis.fluent.base.BaseDaoImpl;
import cn.org.atool.mybatis.fluent.demo.entity.UserEntity;
import cn.org.atool.mybatis.fluent.demo.mapper.UserMapper;
import cn.org.atool.mybatis.fluent.demo.mapping.UserMP;
import cn.org.atool.mybatis.fluent.demo.query.UserEntityQuery;
import cn.org.atool.mybatis.fluent.demo.query.UserEntityUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import cn.org.atool.mybatis.fluent.demo.MyCustomerInterface;

/**
* UserEntity数据库操作服务类
 *
 * @author generate code
*/
public abstract class UserBaseDao extends BaseDaoImpl<UserEntity, UserEntityQuery, UserEntityUpdate>
        implements UserMP, MyCustomerInterface<UserEntity, UserEntityQuery, UserEntityUpdate> {

    @Autowired
    protected UserMapper mapper;

    @Override
    public UserMapper mapper() {
        return mapper;
    }

    @Override
    public UserEntityQuery query(){
        return new UserEntityQuery();
    }

    @Override
    public UserEntityUpdate update(){
        return new UserEntityUpdate();
    }

    @Override
    public String findPkColumn() {
        return UserMP.Column.id;
    }
}
