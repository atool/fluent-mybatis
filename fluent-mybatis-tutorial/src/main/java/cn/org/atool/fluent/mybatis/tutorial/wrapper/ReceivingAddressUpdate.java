package cn.org.atool.fluent.mybatis.tutorial.wrapper;

import cn.org.atool.fluent.mybatis.base.impl.BaseUpdate;

import cn.org.atool.fluent.mybatis.tutorial.entity.ReceivingAddressEntity;
import cn.org.atool.fluent.mybatis.tutorial.helper.ReceivingAddressMapping;
import cn.org.atool.fluent.mybatis.tutorial.helper.ReceivingAddressWrapperHelper.UpdateSetter;
import cn.org.atool.fluent.mybatis.tutorial.helper.ReceivingAddressWrapperHelper.UpdateWhere;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * ReceivingAddressUpdate: ReceivingAddressEntity更新设置
 *
 * @author generate code
 */
public class ReceivingAddressUpdate extends BaseUpdate<ReceivingAddressEntity, ReceivingAddressUpdate, ReceivingAddressQuery> {
    /**
     * 更新条件设置
     */
    public final UpdateWhere where = new UpdateWhere(this);
    /**
     * 更新值设置
     */
    public final UpdateSetter set = new UpdateSetter(this);

    public ReceivingAddressUpdate(){
        super(ReceivingAddressMapping.Table_Name, ReceivingAddressEntity.class, ReceivingAddressQuery.class);
    }

    @Override
    public UpdateWhere where() {
        return this.where;
    }

    @Override
    protected void validateColumn(String column) throws FluentMybatisException {
        if (isNotEmpty(column) && !ReceivingAddressMapping.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + ReceivingAddressMapping.Table_Name + "].");
        }
    }
}