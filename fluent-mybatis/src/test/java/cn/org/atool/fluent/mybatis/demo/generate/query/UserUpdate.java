package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.base.*;

import java.util.*;

import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.UserMP;
import cn.org.atool.fluent.mybatis.demo.generate.helper.UserEntityHelper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserWrapperHelper.WrapperWhere;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserWrapperHelper.UpdateSetter;

/**
 * UserUpdate: UserEntity更新设置
 *
 * @author generate code
 */
public class UserUpdate extends BaseUpdate<UserEntity, UserUpdate, UserQuery> {

    public final WrapperWhere<UserUpdate> and = new WrapperWhere<>(this);

    public final WrapperWhere<UserUpdate> or = new WrapperWhere<>(this, false);

    public final UpdateSetter set = new UpdateSetter(this);

    public UserUpdate(){
        super(UserEntity.class);
    }

    @Override
    public UserUpdate eqByNotNull(UserEntity entity) {
        super.eqByNotNull(UserEntityHelper.column(entity));
        return this;
    }

    @Override
    protected Map<String, String> property2Column() {
        return UserMP.Property2Column;
    }

    @Override
    public Class<UserQuery> queryClass() {
        return UserQuery.class ;
    }
}