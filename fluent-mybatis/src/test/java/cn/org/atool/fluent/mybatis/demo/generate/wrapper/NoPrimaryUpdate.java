package cn.org.atool.fluent.mybatis.demo.generate.wrapper;

import cn.org.atool.fluent.mybatis.base.impl.BaseUpdate;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoPrimaryMapping;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoPrimaryWrapperHelper.UpdateSetter;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoPrimaryWrapperHelper.UpdateWhere;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;

/**
 * NoPrimaryUpdate: NoPrimaryEntity更新设置
 *
 * @author generate code
 */
public class NoPrimaryUpdate extends BaseUpdate<NoPrimaryEntity, NoPrimaryUpdate, NoPrimaryQuery> {
    /**
     * 更新条件设置
     */
    public final UpdateWhere where = new UpdateWhere(this);
    /**
     * 更新值设置
     */
    public final UpdateSetter set = new UpdateSetter(this);

    public NoPrimaryUpdate(){
        super(NoPrimaryMapping.Table_Name, NoPrimaryEntity.class, NoPrimaryQuery.class);
    }

    @Override
    public UpdateWhere where() {
        return this.where;
    }

    @Override
    protected void validateColumn(String column) throws FluentMybatisException {
        if (isNotBlank(column) && !NoPrimaryMapping.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + NoPrimaryMapping.Table_Name + "].");
        }
    }
}