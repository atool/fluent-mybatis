package cn.org.atool.fluent.mybatis.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

import static cn.org.atool.fluent.mybatis.model.FormItemOp.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * 表单项
 *
 * @author darui.wu
 */
@Data
@Accessors(chain = true)
public class FormItem implements Serializable {
    private static final long serialVersionUID = 5691660056854381559L;
    /**
     * 条件项key
     */
    private String key;
    /**
     * 操作符:
     * gt, ge, eq, le, lt
     * like(not like), between(not between), in(not in)
     */
    private String op;
    /**
     * 条件值
     * between [min, max]
     * in [item1, item2, ..., itemN]
     */
    private String value;

    public FormItem() {
    }

    public FormItem(String key, String op, String value) {
        this.key = key;
        this.op = op;
        this.value = value;
        this.validate();
    }

    private void validate() {
        assertNotBlank("key", key);
        assertNotBlank("op", op);
        if (!ALL_OP.contains(op)) {
            throw new RuntimeException("only support operation:" + String.join(", ", ALL_OP) + ", but find:" + op);
        }
        if (OP_BETWEEN.equals(op) ||
            OP_NOT_BETWEEN.equals(op) ||
            OP_IN.equals(op) ||
            OP_NOT_IN.equals(op)) {
            assertNotNull("value", value);
            if (!value.startsWith("[") || !value.endsWith("]")) {
                throw new RuntimeException("The range[" + op + "] value must start with '[' and end with ']'.");
            }
        } else if (!OP_IS_NULL.equals(op) &&
            !OP_NOT_NULL.equals(op)) {
            assertNotNull("value", value);
        }
    }

    public Object[] paras() {
        if (OP_BETWEEN.equals(op) ||
            OP_NOT_BETWEEN.equals(op) ||
            OP_IN.equals(op) ||
            OP_NOT_IN.equals(op)) {
            return value.substring(1, value.length() - 1).split(",");
        } else {
            return new Object[]{value};
        }
    }
}