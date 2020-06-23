package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.interfaces.base.BaseUpdate;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoAutoIdWrapperHelper.UpdateSetter;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoAutoIdWrapperHelper.UpdateWhere;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * NoAutoIdUpdate: NoAutoIdEntity更新设置
 *
 * @author generate code
 */
public class NoAutoIdUpdate extends BaseUpdate<NoAutoIdEntity, NoAutoIdUpdate, NoAutoIdQuery> {
    /**
     * 更新条件设置
     */
    public final UpdateWhere where = new UpdateWhere(this);
    /**
     * 更新值设置
     */
    public final UpdateSetter set = new UpdateSetter(this);

    public NoAutoIdUpdate(){
        super(NoAutoIdMP.Table_Name, NoAutoIdEntity.class, NoAutoIdQuery.class);
    }

    @Override
    public UpdateWhere where() {
        return this.where;
    }

    @Override
    protected void validateColumn(String column) throws FluentMybatisException {
        if (isNotEmpty(column) && !NoAutoIdMP.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + NoAutoIdMP.Table_Name + "].");
        }
    }
}