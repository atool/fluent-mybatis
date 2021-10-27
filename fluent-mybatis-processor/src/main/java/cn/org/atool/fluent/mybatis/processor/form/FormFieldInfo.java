package cn.org.atool.fluent.mybatis.processor.form;

import cn.org.atool.fluent.form.annotation.EntryType;
import lombok.Data;

@Data
public class FormFieldInfo {
    private String entryName;

    private String fieldName;

    private String getterName;

    private String setterName;

    private EntryType type;

    private boolean ignoreNull;

    public FormFieldInfo(String entryName, String fieldName) {
        this.entryName = entryName;
        this.fieldName = fieldName;
    }

    public void setEntryType(EntryType type, boolean ignoreNull) {
        this.type = type;
        this.ignoreNull = ignoreNull;
    }
}