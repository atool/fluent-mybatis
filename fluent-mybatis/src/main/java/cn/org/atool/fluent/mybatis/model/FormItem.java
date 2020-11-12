package cn.org.atool.fluent.mybatis.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.model.FormItemOp.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotEmpty;

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
    private Object[] value;

    public FormItem() {
    }

    public FormItem(String key, String op, Object... value) {
        this.key = key;
        this.op = op;
        this.value = value;
        this.validate();
    }

    private void validate() {
        assertNotBlank("key", key);
        if (isBlank(op)) {
            op = OP_EQ;
        } else if (ALL_OP.contains(op)) {
            throw new RuntimeException("only support operation:" + String.join(", ", ALL_OP) + ", but find:" + op);
        }
        if (OP_BETWEEN.equals(op) || OP_NOT_BETWEEN.equals(op)) {
            assertNotEmpty("value", value);
            if (value.length != 2) {
                throw new RuntimeException("The number of between operation parameters[" + key + "] must be two.");
            }
        } else if (OP_IN.equals(op) || OP_NOT_IN.equals(op)) {
            assertNotEmpty("parameter of " + key, value);
        } else if (!OP_IS_NULL.equals(op) && !OP_NOT_NULL.equals(op)) {
            assertNotEmpty("parameter of " + key, value);
            if (value.length != 1) {
                throw new RuntimeException("The number of parameters[" + key + "] must be one.");
            }
        }
    }
}