package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.meta.entry.PagedEntry;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.form.annotation.EntryType.*;

/**
 * PagedMetaKit
 * @author powered by FluentMybatis
 */
@SuppressWarnings({"unused"})
public class PagedEntryMetaKit implements EntryMetaKit {
  private static final List<EntryMeta> metas = new ArrayList<>(3);

  static {
    metas.add(new EntryMeta("pageSize", PageSize, PagedEntry::getPageSize, PagedEntry::setPageSize, true));
    metas.add(new EntryMeta("currPage", CurrPage, PagedEntry::getCurrPage, PagedEntry::setCurrPage, true));
    metas.add(new EntryMeta("pagedTag", PagedTag, PagedEntry::getPagedTag, PagedEntry::setPagedTag, true));
  }

  @Override
  public final List<EntryMeta> entryMetas() {
    return metas;
  }
}
