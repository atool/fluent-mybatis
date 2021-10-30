package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.form.annotation.FormEntry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.Form;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Form
@Data
@Accessors(chain = true)
public class StudentUpdater implements Serializable {
    @FormEntry(type = EntryType.Update)
    private String userName;

    @FormEntry(type = EntryType.Update)
    private Integer age;

    private Long id;
}