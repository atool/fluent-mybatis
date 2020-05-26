package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import cn.org.atool.fluent.mybatis.method.normal.*;
import cn.org.atool.fluent.mybatis.method.partition.DeleteSpec;
import cn.org.atool.fluent.mybatis.method.partition.SelectSpec;
import cn.org.atool.fluent.mybatis.method.partition.UpdateSpecByQuery;

import java.util.Arrays;
import java.util.List;

/**
 * MyXmlMethod
 *
 * @author:darui.wu Created by darui.wu on 2020/5/25.
 */
public interface InjectMethod {
    /**
     * 方法id
     *
     * @return
     */
    String statementId();

    /**
     * 构建注入方法的sql语句片段
     *
     * @param entity 实体类
     * @param table  表信息
     * @return sql语句片段
     */
    String getMethodSql(Class entity, TableInfo table);

    /**
     * fluent mybatis内置方法
     *
     * @return
     */
    static List<InjectMethod> injectMethods() {
        return Arrays.asList(
            // delete
            new Delete(),
            new DeleteById(),
            new DeleteByIds(),
            new DeleteByMap(),
            // insert
            new Insert(),
            new InsertBatch(),
            // select
            new SelectById(),
            new SelectByIds(),
            new SelectByMap(),
            new SelectCount(),
            new SelectList(),
            new SelectMaps(),
            new SelectObjs(),
            new SelectOne(),
            // update
            new UpdateById(),
            new UpdateByQuery(),

            // partition
            new DeleteSpec(),
            new SelectSpec(),
            new UpdateSpecByQuery()
        );
    }
}