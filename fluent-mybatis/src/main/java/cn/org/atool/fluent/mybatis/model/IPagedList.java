package cn.org.atool.fluent.mybatis.model;

import java.util.List;

/**
 * 分页查询结果
 *
 * @param <E>
 */
public interface IPagedList<E> {
    /**
     * 分页查询返回数据
     *
     * @return entity list
     */
    List<E> getData();

    /**
     * @return next
     */
    default E getNext() {
        return null;
    }

    /**
     * 数据总数, -1表示没有计算总数
     *
     * @return 总数
     */
    default int getTotal() {
        return -1;
    }
}