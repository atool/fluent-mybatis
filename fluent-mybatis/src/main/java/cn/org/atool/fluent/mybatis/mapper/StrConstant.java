package cn.org.atool.fluent.mybatis.mapper;

/**
 * fluent mybatis 常量定义
 *
 * @author darui.wu
 */
public interface StrConstant {
    /**
     * 空串
     */
    String EMPTY = "" ;
    /**
     * where
     */
    String WHERE = "WHERE" ;
    /**
     * distinct
     */
    String DISTINCT = " DISTINCT " ;
    /**
     * asc
     */
    String ASC = "ASC" ;
    /**
     * desc
     */
    String DESC = "DESC" ;
    /**
     * 格式化占位符
     */
    String STR_FORMAT = "%s" ;
    /**
     * 逗号
     */
    String COMMA = "," ;
    /**
     * 星号
     */
    String ASTERISK = "*" ;
    /**
     * 问号
     */
    String QUESTION_MARK = "?" ;
    /**
     * 空格
     */
    String SPACE = " " ;
    /**
     * 换行
     */
    String NEWLINE = "\n" ;
    /**
     * 逗号 + 空格
     */
    String COMMA_SPACE = COMMA + SPACE;
    /**
     * select ... from ... where ...
     */
    String SELECT_FROM_WHERE = "SELECT %s FROM %s WHERE %s" ;
}