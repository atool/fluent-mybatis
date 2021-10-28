package cn.org.atool.fluent.form.processor;

import cn.org.atool.fluent.form.annotation.EntryType;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Objects;

/**
 * FormFieldInfo: 表单字段定义
 *
 * @author wudarui
 */
@Getter
public class FormFieldInfo {
    /**
     * 表单项, 对应Entity属性名称
     */
    private final String entryName;
    /**
     * 字段名称
     */
    private final String fieldName;
    /**
     * 字段类型
     */
    private final String fieldType;

    @Getter(AccessLevel.NONE)
    private final String capital;
    /**
     * 表单项类别
     */
    private EntryType entryType;
    /**
     * 是否忽略空值
     */
    private boolean ignoreNull;

    public FormFieldInfo(String entryName, String fieldName, String fieldType) {
        this.entryName = entryName;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        if (!Objects.equals(fieldType, boolean.class.getName())) {
            this.capital = this.capital(fieldName, 0);
        } else if (fieldName.startsWith("is")) {
            this.capital = this.capital(fieldName, 2);
        } else {
            this.capital = this.capital(fieldName, 0);
        }
    }

    private String capital(String text, int index) {
        return text.substring(index, index + 1).toUpperCase() + text.substring(index + 1);
    }

    public void setEntryType(EntryType type, boolean ignoreNull) {
        this.entryType = type;
        this.ignoreNull = ignoreNull;
    }

    public String getterName() {
        if (Objects.equals(fieldType, boolean.class.getName())) {
            return "is" + this.capital;
        } else {
            return "get" + this.capital;
        }
    }

    public String setterName() {
        return "set" + this.capital;
    }
}