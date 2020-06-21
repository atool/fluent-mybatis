package cn.org.atool.fluent.mybatis.condition.model;

/**
 * fluent mybatis 常量定义
 *
 * @author darui.wu
 */
public interface Constants {
    /**
     * wrapper 类
     */
    String WRAPPER = "ew";
    /**
     * columnMap
     */
    String COLUMN_MAP = "cm";

    /**
     * collection
     */
    String COLLECTION = "coll";
    /**
     * 实体类
     */
    String ENTITY = "et";
    /**
     * 分库
     */
    String SPEC_COMMENT = "SPEC_COMMENT";
    /**
     * 分表
     */
    String SPEC_TABLE = "SPEC_TABLE";
    /**
     * 空串
     */
    String EMPTY = "";
    /**
     * where
     */
    String WHERE = "WHERE";
    /**
     * distinct
     */
    String DISTINCT = " DISTINCT ";
    /**
     * asc
     */
    String ASC = "ASC";
    /**
     * desc
     */
    String DESC = "DESC";
    /**
     * 格式化占位符
     */
    String STR_FORMAT = "%s";
    /**
     * 逗号
     */
    String COMMA = ",";
    /**
     * 星号
     */
    String ASTERISK = "*";
    /**
     * 问号
     */
    String QUESTION_MARK = "?";
    /**
     * 空格
     */
    String SPACE = " ";
    /**
     * 逗号 + 空格
     */
    String COMMA_SPACE = COMMA + SPACE;
}