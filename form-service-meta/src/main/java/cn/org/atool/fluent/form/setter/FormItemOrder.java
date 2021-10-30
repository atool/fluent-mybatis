package cn.org.atool.fluent.form.setter;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 字段排序
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused"})
@Data
@Accessors(chain = true)
public class FormItemOrder implements Serializable {
    private String field;

    private boolean asc = true;

    public FormItemOrder() {
    }

    public FormItemOrder(String field, boolean asc) {
        this.field = field;
        this.asc = asc;
    }
}