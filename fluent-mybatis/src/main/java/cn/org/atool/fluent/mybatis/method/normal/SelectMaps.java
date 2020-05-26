package cn.org.atool.fluent.mybatis.method.normal;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_SelectMaps;

/**
 * SelectMaps:  查询满足条件所有数据
 *
 * @author darui.wu
 * @create 2020/5/18 1:45 下午
 */
public class SelectMaps extends SelectList {
    @Override
    public String statementId() {
        return Method_SelectMaps;
    }

    @Override
    protected Class resultType() {
        return Map.class;
    }
}