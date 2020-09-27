package cn.org.atool.fluent.mybatis.test.method.normal;

import cn.org.atool.fluent.mybatis.test.method.metadata.DbType;

import static cn.org.atool.fluent.mybatis.test.method.model.StatementId.Method_SelectObjs;

/**
 * SelectObjs: 查询满足条件所有数据,只返回第一个字段的值
 *
 * @author darui.wu
 * @create 2020/5/18 2:09 下午
 */
public class SelectObjs extends SelectList {
    public SelectObjs(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return Method_SelectObjs;
    }

    @Override
    protected Class resultType() {
        return Object.class;
    }
}