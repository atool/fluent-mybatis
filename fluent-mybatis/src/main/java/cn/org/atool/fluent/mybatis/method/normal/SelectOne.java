package cn.org.atool.fluent.mybatis.method.normal;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_SelectOne;

/**
 * SelectOne: 查询满足条件一条数据
 *
 * @author darui.wu
 * @create 2020/5/18 11:18 上午
 */
public class SelectOne extends SelectList {
    @Override
    public String statementId() {
        return Method_SelectOne;
    }
}