package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.Entry;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分页表单项
 *
 * @author darui.wu
 */
@SuppressWarnings("unused")
@Data
@Accessors(chain = true)
public class PagedEntry {
    @Entry(type = EntryType.PageSize)
    private int pageSize;

    @Entry(type = EntryType.CurrPage)
    private Integer currPage;

    @Entry(type = EntryType.PagedTag)
    private String pagedTag;
}