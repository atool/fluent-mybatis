package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.metadata.DbType;

import static cn.org.atool.fluent.mybatis.method.partition.StatementId.Method_SelectListFromSpec;

/**
 * SelectSpec : 从指定表（分表）查询
 *
 * @author darui.wu
 */
public class SelectSpec extends SelectList {
    public SelectSpec(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return Method_SelectListFromSpec;
    }

    protected boolean isSpecTable() {
        return true;
    }
}