package cn.org.atool.fluent.mybatis.demo.generate.wrapper;

import cn.org.atool.fluent.mybatis.base.impl.BaseUpdate;

import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.UserMapping;
import cn.org.atool.fluent.mybatis.demo.generate.helper.UserWrapperHelper.*;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;

/**
 * UserUpdate: UserEntity更新设置
 *
 * @author generate code
 */
public class UserUpdate extends BaseUpdate<UserEntity, UserUpdate, UserQuery> {
    /**
     * 更新值设置
     */
    public final UpdateSetter update = new UpdateSetter(this);
    /**
     * 使用update变量, 后续版本将删除
     */
    @Deprecated
    public final UpdateSetter set = update;
    /**
     * 更新条件设置
     */
    public final UpdateWhere where = new UpdateWhere(this);
    /**
     * order by
     */
    public final UpdateOrderBy orderBy = new UpdateOrderBy(this);

    public UserUpdate() {
        super(UserMapping.Table_Name, UserEntity.class, UserQuery.class);
    }

    @Override
    public UpdateWhere where() {
        return this.where;
    }

    @Override
    protected boolean hasPrimary() {
        return true;
    }

    @Override
    protected void validateColumn(String column) throws FluentMybatisException {
        if (isNotBlank(column) && !UserMapping.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + UserMapping.Table_Name + "].");
        }
    }
}