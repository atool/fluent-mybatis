package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.base.*;

import java.util.*;

import cn.org.atool.fluent.mybatis.demo.generate.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP;
import cn.org.atool.fluent.mybatis.demo.generate.helper.AddressEntityHelper;
import cn.org.atool.fluent.mybatis.demo.generate.query.AddressWrapperHelper.WrapperWhere;
import cn.org.atool.fluent.mybatis.demo.generate.query.AddressWrapperHelper.UpdateSetter;

/**
 * AddressUpdate: AddressEntity更新设置
 *
 * @author generate code
 */
public class AddressUpdate extends BaseUpdate<AddressEntity, AddressUpdate, AddressQuery> {

    public final WrapperWhere<AddressUpdate> and = new WrapperWhere<>(this);

    public final WrapperWhere<AddressUpdate> or = new WrapperWhere<>(this, false);

    public final UpdateSetter set = new UpdateSetter(this);

    public AddressUpdate(){
        super(AddressEntity.class);
    }

    @Override
    public AddressUpdate eqByNotNull(AddressEntity entity) {
        super.eqByNotNull(AddressEntityHelper.column(entity));
        return this;
    }

    @Override
    protected Map<String, String> property2Column() {
        return AddressMP.Property2Column;
    }

    @Override
    public Class<AddressQuery> queryClass() {
        return AddressQuery.class ;
    }
}