package cn.org.atool.fluent.mybatis.metadata.feature;

import static cn.org.atool.fluent.mybatis.If.isBlank;

/**
 * 各类型数据库的分页方式
 * 格式由4个变量组成, 其中{query}是必须变量, {offset}/{size}/{end} 这3个量选2个
 * <p>
 * o {query}, 查询sql语句主体
 * o {offset}, 分页开始位移
 * o {size}, 分页查询数量
 * o {end}, 分页结束位移
 *
 * @author darui.wu
 */
public final class PagedExpress {
    private final String format;

    public PagedExpress(String format) {
        this.format = format;
    }

    /**
     * 不支持分页
     */
    public static final PagedExpress UN_SUPPORT_LIMIT = new PagedExpress(null);
    /**
     * MYSQL语法分页
     */
    public static final PagedExpress MYSQL_LIMIT = new PagedExpress("{query} LIMIT {offset}, {size}");
    /**
     * PG语法分页
     */
    public static final PagedExpress PG_LIMIT = new PagedExpress("{query} LIMIT {size} OFFSET {offset}");
    /**
     * DB2语法分页
     */
    public static final PagedExpress DB2_LIMIT = new PagedExpress(
        "SELECT * FROM " +
            "(SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ({query})) AS TMP_PAGE) TMP_PAGE " +
            "WHERE ROW_ID BETWEEN {offset} AND {size}");
    /**
     * ORACLE语法分页
     */
    public static final PagedExpress ORACLE_LIMIT = new PagedExpress(
        "SELECT * FROM ( " +
            " SELECT TMP_PAGE.*, ROWNUM ROW_ID FROM ({query}) TMP_PAGE) " +
            " WHERE ROW_ID > {offset} AND ROW_ID <= {end}");

    public String build(String query, String pagedOffset, String pagedSize, String pagedEndOffset) {
        if (isBlank(format)) {
            throw new RuntimeException("not support");
        }
        return this.format.replace("{end}", pagedEndOffset)
            .replace("{size}", pagedSize)
            .replace("{offset}", pagedOffset)
            .replace("{query}", query);
    }
}