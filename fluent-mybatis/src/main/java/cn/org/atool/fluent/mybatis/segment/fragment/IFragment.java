package cn.org.atool.fluent.mybatis.segment.fragment;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;

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
     * @param mapping 实体映射
     * @return ignore
     */
    String get(IMapping mapping);

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