package cn.org.atool.fluent.form.kits;

import cn.org.atool.fluent.form.meta.EntryMeta;
import cn.org.atool.fluent.form.meta.entry.EntryMetaKit;
import cn.org.atool.fluent.form.meta.entry.PagedEntry;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.form.annotation.EntryType.*;

/**
 * PagedMetaKit
 *
 * @author powered by FluentMybatis
 */
@SuppressWarnings({"unused"})
public class PagedEntryMetaKit implements EntryMetaKit {
    private static final List<EntryMeta> metas = new ArrayList<>(3);

    static {
        metas.add(new EntryMeta("pageSize", int.class, PageSize, PagedEntry::getPageSize, null, true));
        metas.add(new EntryMeta("currPage", int.class, CurrPage, PagedEntry::getCurrPage, null, true));
        metas.add(new EntryMeta("pagedTag", String.class, PagedTag, PagedEntry::getPagedTag, null, true));
    }

    @Override
    public final List<EntryMeta> entryMetas() {
        return metas;
    }
}