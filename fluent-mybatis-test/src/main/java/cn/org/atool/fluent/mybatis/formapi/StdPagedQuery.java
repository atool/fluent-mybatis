package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.form.annotation.FormEntry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.Form;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Form
@Data
@Accessors(chain = true)
public class StdPagedQuery extends StudentQueryApi.StudentQuery {
    @FormEntry(type = EntryType.PageSize)
    private int pageSize = 10;

    @FormEntry(type = EntryType.CurrPage)
    private int currPage = 0;
}
