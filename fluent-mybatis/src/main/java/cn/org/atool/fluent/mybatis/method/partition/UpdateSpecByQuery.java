package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.method.normal.UpdateByQuery;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_UpdateSpecByQuery;

/**
 * UpdateSpecByQuery : 更新指定分表
 *
 * @author darui.wu
 */
public class UpdateSpecByQuery extends UpdateByQuery {
    @Override
    public String statementId() {
        return Method_UpdateSpecByQuery;
    }

    @Override
    protected boolean isSpecTable() {
        return true;
    }
}