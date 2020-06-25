package cn.org.atool.fluent.mybatis.segment.model;


import java.io.Serializable;

/**
 * SQL 片段接口
 *
 * @author darui.wu
 */
@FunctionalInterface
public interface ISqlSegment extends Serializable {

    /**
     * SQL 片段
     *
     * @return ignore
     */
    String getSqlSegment();

    /**
     * 是否 AND 或者 OR
     *
     * @return true:AND OR
     */
    default boolean isAndOr() {
        return false;
    }
}