package cn.org.atool.fluent.form;

/**
 * 分页标识
 *
 * @author darui.wu
 */
public interface IPaged {
    /**
     * 每页limit数量
     *
     * @return pageSize, 如果非分页或limit操作, 返回null
     */
    int getPageSize();

    /**
     * 当前分页号, 默认值0
     *
     * @return 当前分页号
     */
    default Integer getCurrPage() {
        return null;
    }

    /**
     * 返回Tag分页方式的起始主键值
     *
     * @return 起始主键值, 非tag分页返回null
     */
    default String getStartPk() {
        return null;
    }
}