package cn.org.atool.fluent.mybatis.test.method.partition;

import cn.org.atool.fluent.mybatis.test.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.test.method.normal.SelectList;

import static cn.org.atool.fluent.mybatis.test.method.model.StatementId.Method_SelectListFromSpec;

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

    @Override
    protected boolean isSpecTable() {
        return true;
    }
}