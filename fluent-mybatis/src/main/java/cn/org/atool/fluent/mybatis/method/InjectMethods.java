package cn.org.atool.fluent.mybatis.method;

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
     * 注入的方法列表
     * fluent mybatis内置方法
     *
     * @return
     */
    default List<InjectMethod> methods() {
        return Arrays.asList(
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
            // delete
            new Delete(),
            new DeleteById(),
            new DeleteByIds(),
            new DeleteByMap(),
            // update
            new UpdateById(),
            new UpdateByQuery(),
            // partition
            new DeleteSpec(),
            new SelectSpec(),
            new UpdateSpecByQuery()
        );
    }

    /**
     * 默认注入方法
     */
    final class DefaultInjectMethods implements InjectMethods {
    }
}