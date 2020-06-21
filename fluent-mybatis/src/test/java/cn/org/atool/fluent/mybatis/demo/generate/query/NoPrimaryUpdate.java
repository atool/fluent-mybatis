package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.base.*;

import java.util.*;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoPrimaryEntityHelper;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoPrimaryWrapperHelper.WrapperWhere;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoPrimaryWrapperHelper.UpdateSetter;

/**
 * NoPrimaryUpdate: NoPrimaryEntity更新设置
 *
 * @author generate code
 */
public class NoPrimaryUpdate extends BaseUpdate<NoPrimaryEntity, NoPrimaryUpdate, NoPrimaryQuery> {

    public final WrapperWhere<NoPrimaryUpdate> and = new WrapperWhere<>(this);

    public final WrapperWhere<NoPrimaryUpdate> or = new WrapperWhere<>(this, false);

    public final UpdateSetter set = new UpdateSetter(this);

    public NoPrimaryUpdate(){
        super(NoPrimaryEntity.class);
    }

    @Override
    public NoPrimaryUpdate eqByNotNull(NoPrimaryEntity entity) {
        super.eqByNotNull(NoPrimaryEntityHelper.column(entity));
        return this;
    }

    @Override
    protected Map<String, String> property2Column() {
        return NoPrimaryMP.Property2Column;
    }

    @Override
    public Class<NoPrimaryQuery> queryClass() {
        return NoPrimaryQuery.class ;
    }
}