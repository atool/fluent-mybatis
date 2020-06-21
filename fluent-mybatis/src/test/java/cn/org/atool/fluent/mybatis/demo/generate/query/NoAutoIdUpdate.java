package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.base.*;

import java.util.*;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoAutoIdEntityHelper;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoAutoIdWrapperHelper.WrapperWhere;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoAutoIdWrapperHelper.UpdateSetter;

/**
 * NoAutoIdUpdate: NoAutoIdEntity更新设置
 *
 * @author generate code
 */
public class NoAutoIdUpdate extends BaseUpdate<NoAutoIdEntity, NoAutoIdUpdate, NoAutoIdQuery> {

    public final WrapperWhere<NoAutoIdUpdate> and = new WrapperWhere<>(this);

    public final WrapperWhere<NoAutoIdUpdate> or = new WrapperWhere<>(this, false);

    public final UpdateSetter set = new UpdateSetter(this);

    public NoAutoIdUpdate(){
        super(NoAutoIdEntity.class);
    }

    @Override
    public NoAutoIdUpdate eqByNotNull(NoAutoIdEntity entity) {
        super.eqByNotNull(NoAutoIdEntityHelper.column(entity));
        return this;
    }

    @Override
    protected Map<String, String> property2Column() {
        return NoAutoIdMP.Property2Column;
    }

    @Override
    public Class<NoAutoIdQuery> queryClass() {
        return NoAutoIdQuery.class ;
    }
}