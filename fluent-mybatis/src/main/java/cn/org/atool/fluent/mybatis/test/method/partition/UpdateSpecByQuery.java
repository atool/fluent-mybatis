package cn.org.atool.fluent.mybatis.test.method.partition;

import cn.org.atool.fluent.mybatis.test.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.test.method.normal.UpdateByQuery;

import static cn.org.atool.fluent.mybatis.test.method.model.StatementId.Method_UpdateSpecByQuery;

/**
 * UpdateSpecByQuery : 更新指定分表
 *
 * @author darui.wu
 */
public class UpdateSpecByQuery extends UpdateByQuery {
    public UpdateSpecByQuery(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return Method_UpdateSpecByQuery;
    }

    @Override
    protected boolean isSpecTable() {
        return true;
    }
}