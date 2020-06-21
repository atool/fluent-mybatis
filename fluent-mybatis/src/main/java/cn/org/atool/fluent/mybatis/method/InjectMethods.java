package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.normal.*;
import cn.org.atool.fluent.mybatis.method.partition.DeleteSpec;
import cn.org.atool.fluent.mybatis.method.partition.SelectSpec;
import cn.org.atool.fluent.mybatis.method.partition.UpdateSpecByQuery;

import java.util.Arrays;
import java.util.List;

/**
 * InjectMethods
 *
 * @author:darui.wu Created by darui.wu on 2020/5/28.
 */
public interface InjectMethods {
    /**
     * 内置方法列表（不包含分库分表）
     * fluent mybatis内置方法
     *
     * @param dbType 数据库类型
     * @return
     */
    default List<InjectMethod> methods(DbType dbType) {
        return Arrays.asList(
            // insert
            new Insert(dbType),
            new InsertBatch(dbType),
            // select
            new SelectById(dbType),
            new SelectByIds(dbType),
            new SelectByMap(dbType),
            new SelectList(dbType),
            new SelectMaps(dbType),
            new SelectObjs(dbType),
            new SelectOne(dbType),
            new SelectCount(dbType),
            new CountNoLimit(dbType),
            // delete
            new Delete(dbType),
            new DeleteById(dbType),
            new DeleteByIds(dbType),
            new DeleteByMap(dbType),
            // update
            new UpdateById(dbType),
            new UpdateByQuery(dbType)
        );
    }

    /**
     * 分库分表的方法
     *
     * @param dbType 数据库类型
     * @return
     */
    default List<InjectMethod> sharingMethods(DbType dbType) {
        return Arrays.asList(
            new DeleteSpec(dbType),
            new SelectSpec(dbType),
            new UpdateSpecByQuery(dbType)
        );
    }

    /**
     * 默认注入方法
     */
    final class DefaultInjectMethods implements InjectMethods {
    }
}