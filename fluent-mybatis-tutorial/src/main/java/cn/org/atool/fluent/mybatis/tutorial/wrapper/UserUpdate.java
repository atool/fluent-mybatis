package cn.org.atool.fluent.mybatis.tutorial.wrapper;

import cn.org.atool.fluent.mybatis.base.impl.BaseUpdate;

import cn.org.atool.fluent.mybatis.tutorial.entity.UserEntity;
import cn.org.atool.fluent.mybatis.tutorial.helper.UserMapping;
import cn.org.atool.fluent.mybatis.tutorial.helper.UserWrapperHelper.UpdateSetter;
import cn.org.atool.fluent.mybatis.tutorial.helper.UserWrapperHelper.UpdateWhere;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * UserUpdate: UserEntity更新设置
 *
 * @author generate code
 */
public class UserUpdate extends BaseUpdate<UserEntity, UserUpdate, UserQuery> {
    /**
     * 更新条件设置
     */
    public final UpdateWhere where = new UpdateWhere(this);
    /**
     * 更新值设置
     */
    public final UpdateSetter set = new UpdateSetter(this);

    public UserUpdate(){
        super(UserMapping.Table_Name, UserEntity.class, UserQuery.class);
    }

    @Override
    public UpdateWhere where() {
        return this.where;
    }

    @Override
    protected void validateColumn(String column) throws FluentMybatisException {
        if (isNotEmpty(column) && !UserMapping.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + UserMapping.Table_Name + "].");
        }
    }
}