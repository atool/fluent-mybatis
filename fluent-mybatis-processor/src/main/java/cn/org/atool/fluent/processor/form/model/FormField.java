package cn.org.atool.fluent.processor.form.model;

import cn.org.atool.fluent.form.annotation.EntryType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Objects;

import static cn.org.atool.fluent.common.kits.StringKit.*;

/**
 * FormFieldInfo: 表单字段定义
 *
 * @author wudarui
 */
@Getter
public class FormField {
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
    private final TypeName fieldType;

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

    public FormField(String entryName, String fieldName, TypeName fieldType) {
        this.entryName = entryName;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        if (!Objects.equals(fieldType, ClassName.BOOLEAN)) {
            this.capital = this.capital(fieldName, 0);
        } else if (fieldName.startsWith(PRE_IS)) {
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
            return PRE_IS + this.capital;
        } else {
            return PRE_GET + this.capital;
        }
    }

    public String setterName() {
        return PRE_SET + this.capital;
    }
}