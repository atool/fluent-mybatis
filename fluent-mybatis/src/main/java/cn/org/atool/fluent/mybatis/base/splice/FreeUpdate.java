package cn.org.atool.fluent.mybatis.base.splice;

import cn.org.atool.fluent.mybatis.base.crud.BaseUpdate;
import cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.UpdateSetter;
import cn.org.atool.fluent.mybatis.segment.WhereBase;

import java.util.List;

import static cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.UpdateOrderBy;
import static cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.UpdateWhere;

public class FreeUpdate extends BaseUpdate<EmptyEntity, FreeUpdate, FreeQuery> {
    public final UpdateSetter update = new UpdateSetter(this);

    public final UpdateWhere where = new UpdateWhere(this);

    public final UpdateOrderBy orderBy = new UpdateOrderBy(this);

    public FreeUpdate(String table) {
        super(table, EmptyEntity.class, FreeQuery.class);
    }

    @Override
    protected List<String> allFields() {
        throw new RuntimeException("not support by FreeUpdate.");
    }

    @Override
    public WhereBase<?, FreeUpdate, FreeQuery> where() {
        return this.where;
    }
}