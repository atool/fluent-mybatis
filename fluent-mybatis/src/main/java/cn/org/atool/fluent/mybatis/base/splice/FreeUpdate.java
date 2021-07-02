package cn.org.atool.fluent.mybatis.base.splice;

import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.crud.BaseUpdate;
import cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.UpdateSetter;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

import static cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.UpdateOrderBy;
import static cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.UpdateWhere;

/**
 * FreeUpdate
 *
 * @author wudarui
 */
@Accessors(chain = true)
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
    public UpdateWhere where() {
        return this.where;
    }

    /**
     * 完全自定义的sql
     * 使用此方法, Query的其它设置(select,where,order,group,limit等)将无效
     *
     * @param sql       用户定义的完整sql语句
     * @param parameter sql参数, 通过#{value} 或 #{field.field}占位
     * @return self
     */
    public FreeUpdate customizedByPlaceholder(String sql, Object parameter) {
        this.wrapperData.customizedSql(sql, parameter);
        return this;
    }

    /**
     * 完全自定义的sql
     * 使用此方法, Query的其它设置(select,where,order,group,limit等)将无效
     *
     * @param sql   用户定义的完整sql语句
     * @param paras sql参数, 通过sql中的'?'占位
     * @return self
     */
    public FreeUpdate customizedByQuestion(String sql, Object... paras) {
        String placeholder = this.wrapperData.paramSql(null, sql, paras);
        this.wrapperData.customizedSql(placeholder, null);
        return this;
    }

    @Setter
    private DbType dbType;

    @Override
    public DbType dbType() {
        return dbType == null ? IRefs.instance().defaultDbType() : dbType;
    }
}