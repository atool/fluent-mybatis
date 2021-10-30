package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.FormEntry;
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
    @FormEntry(type = EntryType.PageSize)
    private int pageSize;

    @FormEntry(type = EntryType.CurrPage)
    private Integer currPage;

    @FormEntry(type = EntryType.PagedTag)
    private String pagedTag;
}