package cn.org.atool.mybatis.fluent.demo.dm;

import cn.org.atool.mybatis.fluent.demo.dm.table.*;
import cn.org.atool.mybatis.fluent.demo.mapping.*;
import org.test4j.module.IScript;

/**
 * 生成内存数据库（h2)脚本
 *
 * @author darui.wu
 * @create 2019-09-02 18:03
 */
public class DataSourceScript implements IScript {
    @Override
    public Class[] getTableKlass() {
        return new Class[]{
                AddressTableMap.class,
                UserTableMap.class
        };
    }

    @Override
    public IndexList getIndexList() {
        return new IndexList();
    }

    static {
        SPEC_TYPES.put("json", "text");
    }
}