package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.model.StatementId;

import java.util.Map;

/**
 * SelectMaps:  查询满足条件所有数据
 *
 * @author darui.wu
 * @create 2020/5/18 1:45 下午
 */
public class SelectMaps extends SelectList {
    public SelectMaps(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return StatementId.Method_SelectMaps;
    }

    @Override
    protected Class resultType() {
        return Map.class;
    }
}