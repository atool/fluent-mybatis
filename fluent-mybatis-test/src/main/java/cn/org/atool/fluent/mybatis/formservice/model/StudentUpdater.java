package cn.org.atool.fluent.mybatis.formservice.model;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.Form;
import lombok.Data;
import lombok.experimental.Accessors;

@Form
@Data
@Accessors(chain = true)
public class StudentUpdater {
    @Entry(type = EntryType.Update)
    private String userName; // "release"

    @Entry(type = EntryType.Update)
    private Integer age;

    private Long id;

    @Entry(value = "userName", type = EntryType.StartWith)
    private String userName22; // like 'test%'
}