package cn.org.atool.fluent.processor.form.model;

import cn.org.atool.fluent.form.annotation.EntryType;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
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

    /**
     * 构造函数
     *
     * @param entryName 表单项名称
     * @param fieldName 字段名称
     * @param fieldType 字段类型
     */
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

    /**
     * 设置表单项类型
     *
     * @param type       表单项类型
     * @param ignoreNull 是否忽略空值
     */
    public void setEntryType(EntryType type, boolean ignoreNull) {
        this.entryType = type;
        this.ignoreNull = ignoreNull;
    }

    /**
     * 获取getter方法名称
     *
     * @return getter name
     */
    public String getterName() {
        if (Objects.equals(fieldType, boolean.class.getName())) {
            return PRE_IS + this.capital;
        } else {
            return PRE_GET + this.capital;
        }
    }

    /**
     * 获取setter方法名称
     *
     * @return setter name
     */
    public String setterName() {
        return PRE_SET + this.capital;
    }
}