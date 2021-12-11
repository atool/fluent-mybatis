package cn.org.atool.fluent.mybatis.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 分页查询结果
 *
 * @author wudarui
 */
@SuppressWarnings({"unused"})
@Getter
@Setter
@Accessors(chain = true)
public class StdPagedList<E> implements IPagedList<E> {
    /**
     * 总记录数
     */
    private int total;

    /**
     * 本次查询结果集
     */
    private List<E> data;

    @Getter(AccessLevel.NONE)
    private boolean hasNextPage;

    public StdPagedList() {
    }

    public StdPagedList(int total, List<E> data) {
        this.total = total;
        this.data = data;
    }

    public StdPagedList(int total, List<E> data, boolean hasNextPage) {
        this.total = total;
        this.data = data;
        this.hasNextPage = hasNextPage;
    }

    public boolean hasNext() {
        return this.hasNextPage;
    }
}