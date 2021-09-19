package cn.org.atool.fluent.mybatis.segment.fragment;


import cn.org.atool.fluent.mybatis.metadata.DbType;

import java.io.Serializable;

/**
 * SQL 片段接口
 *
 * @author darui.wu
 */
@FunctionalInterface
public interface IFragment extends Serializable {

    /**
     * SQL 片段
     *
     * @return ignore
     */
    String get(DbType db);

    default IFragment plus(IFragment segment) {
        return AppendFlag.set(this).append(segment);
    }

    default IFragment plus(String segment) {
        return AppendFlag.set(this).append(segment);
    }

    default boolean notEmpty() {
        return true;
    }
}