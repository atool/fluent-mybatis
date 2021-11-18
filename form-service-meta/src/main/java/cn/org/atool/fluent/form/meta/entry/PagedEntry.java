package cn.org.atool.fluent.form.meta.entry;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 分页表单项
 *
 * @author darui.wu
 */
@SuppressWarnings("unused")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class PagedEntry {
    @Entry(type = EntryType.PageSize)
    private int pageSize;

    @Entry(type = EntryType.CurrPage)
    private Integer currPage;

    @Entry(type = EntryType.PagedTag)
    private String pagedTag;

    public PagedEntry(int currPage, int pageSize) {
        this.pageSize = pageSize;
        this.currPage = currPage;
    }

    public PagedEntry(String pagedTag, int pageSize) {
        this.pageSize = pageSize;
        this.pagedTag = pagedTag;
    }
}