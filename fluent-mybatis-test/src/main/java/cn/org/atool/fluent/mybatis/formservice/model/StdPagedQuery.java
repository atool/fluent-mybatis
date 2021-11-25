package cn.org.atool.fluent.mybatis.formservice.model;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.Form;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Form
@Data
@Accessors(chain = true)
public class StdPagedQuery extends StudentQuery {
    @Entry(type = EntryType.PageSize)
    private int pageSize = 10;

    @Entry(type = EntryType.CurrPage)
    private int currPage = 1;
}