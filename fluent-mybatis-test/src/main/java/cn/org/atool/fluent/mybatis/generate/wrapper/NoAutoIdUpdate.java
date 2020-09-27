package cn.org.atool.fluent.mybatis.generate.wrapper;

import cn.org.atool.fluent.mybatis.base.impl.BaseUpdate;

import cn.org.atool.fluent.mybatis.generate.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.generate.helper.NoAutoIdMapping;
import cn.org.atool.fluent.mybatis.generate.helper.NoAutoIdWrapperHelper.*;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;

/**
 * NoAutoIdUpdate: NoAutoIdEntity更新设置
 *
 * @author generate code
 */
public class NoAutoIdUpdate extends BaseUpdate<NoAutoIdEntity, NoAutoIdUpdate, NoAutoIdQuery> {
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
    *  order by
    */
    public final UpdateOrderBy orderBy = new UpdateOrderBy(this);

    public NoAutoIdUpdate(){
        super(NoAutoIdMapping.Table_Name, NoAutoIdEntity.class, NoAutoIdQuery.class);
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
        if (isNotBlank(column) && !NoAutoIdMapping.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + NoAutoIdMapping.Table_Name + "].");
        }
    }
}