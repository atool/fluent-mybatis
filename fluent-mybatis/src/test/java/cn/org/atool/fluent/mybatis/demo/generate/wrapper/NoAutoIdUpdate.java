package cn.org.atool.fluent.mybatis.demo.generate.wrapper;

import cn.org.atool.fluent.mybatis.base.impl.BaseUpdate;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoAutoIdMapping;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoAutoIdWrapperHelper.UpdateSetter;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoAutoIdWrapperHelper.UpdateWhere;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;

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
        super(NoAutoIdMapping.Table_Name, NoAutoIdEntity.class, NoAutoIdQuery.class);
    }

    @Override
    public UpdateWhere where() {
        return this.where;
    }

    @Override
    protected void validateColumn(String column) throws FluentMybatisException {
        if (isNotBlank(column) && !NoAutoIdMapping.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + NoAutoIdMapping.Table_Name + "].");
        }
    }
}