package cn.org.atool.fluent.mybatis.demo.generate.wrapper;

import cn.org.atool.fluent.mybatis.base.impl.BaseUpdate;

import cn.org.atool.fluent.mybatis.demo.generate.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.AddressMapping;
import cn.org.atool.fluent.mybatis.demo.generate.helper.AddressWrapperHelper.*;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;

/**
 * AddressUpdate: AddressEntity更新设置
 *
 * @author generate code
 */
public class AddressUpdate extends BaseUpdate<AddressEntity, AddressUpdate, AddressQuery> {
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

    public AddressUpdate(){
        super(AddressMapping.Table_Name, AddressEntity.class, AddressQuery.class);
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
        if (isNotBlank(column) && !AddressMapping.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + AddressMapping.Table_Name + "].");
        }
    }
}