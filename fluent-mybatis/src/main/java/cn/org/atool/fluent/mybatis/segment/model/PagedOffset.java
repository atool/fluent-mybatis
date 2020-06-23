package cn.org.atool.fluent.mybatis.segment.model;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * PageOffset: 分页查询设置
 *
 * @author darui.wu
 * @create 2020/6/16 2:00 下午
 */
@Getter
@Accessors(chain = true)
public class PagedOffset {
    /**
     * 查询其实位移
     */
    private long offset = 0;
    /**
     * 查询最大数量
     */
    private long pageSize = 1;

    public PagedOffset() {
    }

    public PagedOffset(long offset, long pageSize) {
        this.setOffset(offset);
        this.setPageSize(pageSize);
    }

    public PagedOffset setOffset(long offset) {
        this.offset = offset < 0 ? 0 : offset;
        return this;
    }

    public PagedOffset setPageSize(long pageSize) {
        this.pageSize = pageSize < 1 ? 1 : pageSize;
        return this;
    }

    public long getEndOffset() {
        return this.offset + this.pageSize;
    }
}