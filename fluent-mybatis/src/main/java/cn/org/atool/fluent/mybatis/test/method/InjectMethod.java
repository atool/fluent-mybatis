package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.test.method.metadata.TableMeta;

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
    String getMethodSql(Class entity, TableMeta table);
}