package cn.org.atool.fluent.mybatis.condition.interfaces;


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
}