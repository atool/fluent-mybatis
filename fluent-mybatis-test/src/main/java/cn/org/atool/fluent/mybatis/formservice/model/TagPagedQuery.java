package cn.org.atool.fluent.mybatis.formservice.model;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TagPagedQuery extends StudentQuery {
    @Entry(type = EntryType.PagedTag)
    private int nextId;

    @Entry(type = EntryType.PageSize)
    private int pageSize = 10;
}