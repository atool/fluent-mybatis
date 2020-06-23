package cn.org.atool.fluent.mybatis.interfaces.model;

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
public class Paged {
    /**
     * 查询其实位移
     */
    private long offset = 0;
    /**
     * 查询最大数量
     */
    private long limit = 1;

    public Paged() {
    }

    public Paged(long offset, long limit) {
        this.setOffset(offset);
        this.setLimit(limit);
    }

    public Paged setOffset(long offset) {
        this.offset = offset < 0 ? 0 : offset;
        return this;
    }

    public Paged setLimit(long limit) {
        this.limit = limit < 1 ? 1 : limit;
        return this;
    }

    public long getEndOffset() {
        return this.offset + this.limit;
    }
}