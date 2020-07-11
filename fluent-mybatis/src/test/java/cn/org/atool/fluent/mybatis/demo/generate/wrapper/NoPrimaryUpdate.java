package cn.org.atool.fluent.mybatis.demo.generate.wrapper;

import cn.org.atool.fluent.mybatis.base.impl.BaseUpdate;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoPrimaryMapping;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoPrimaryWrapperHelper.*;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;

/**
 * NoPrimaryUpdate: NoPrimaryEntity更新设置
 *
 * @author generate code
 */
public class NoPrimaryUpdate extends BaseUpdate<NoPrimaryEntity, NoPrimaryUpdate, NoPrimaryQuery> {
    /**
     * 更新值设置
     */
    public final UpdateSetter set = new UpdateSetter(this);
    /**
    * 更新条件设置
    */
    public final UpdateWhere where = new UpdateWhere(this);
    /**
    *  order by
    */
    public final UpdateOrderBy orderBy = new UpdateOrderBy(this);

    public NoPrimaryUpdate(){
        super(NoPrimaryMapping.Table_Name, NoPrimaryEntity.class, NoPrimaryQuery.class);
    }

    @Override
    public UpdateWhere where() {
        return this.where;
    }

    @Override
    protected boolean hasPrimary() {
        return false;
    }

    @Override
    protected void validateColumn(String column) throws FluentMybatisException {
        if (isNotBlank(column) && !NoPrimaryMapping.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + NoPrimaryMapping.Table_Name + "].");
        }
    }
}