package cn.org.atool.fluent.form.setter;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.model.SqlOpStr;
import cn.org.atool.fluent.mybatis.mapper.StrConstant;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 表单项
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused"})
@Data
@Accessors(chain = true)
public class FormItem implements Serializable {
    private static final long serialVersionUID = 5691660056854381559L;
    /**
     * 条件项key
     */
    private String field;
    /**
     * 操作符:
     * gt, ge, eq, le, lt
     * like(not like), between(not between), in(not in)
     */
    private String op = "EQ";
    /**
     * 条件值
     * between [min, max]
     * in [item1, item2, ..., itemN]
     */
    private Object[] value;

    public FormItem() {
    }

    public FormItem(String item, String op, Object... value) {
        this.field = item;
        this.op = op;
        this.value = value;
        this.validate();
    }

    private void validate() {
        MybatisUtil.assertNotBlank("key", field);
        if (If.isBlank(op)) {
            op = SqlOpStr.OP_EQ;
        } else if (!SqlOpStr.ALL_OP.contains(op)) {
            throw new RuntimeException("only support operation:" + String.join(StrConstant.COMMA_SPACE, SqlOpStr.ALL_OP) + ", but find:" + op);
        }
        if (SqlOpStr.OP_BETWEEN.equals(op) || SqlOpStr.OP_NOT_BETWEEN.equals(op)) {
            MybatisUtil.assertNotEmpty("value", value);
            if (value.length != 2) {
                throw new RuntimeException("The number of between operation parameters[" + field + "] must be two.");
            }
        } else if (SqlOpStr.OP_IN.equals(op) || SqlOpStr.OP_NOT_IN.equals(op)) {
            MybatisUtil.assertNotEmpty("parameter of " + field, value);
        } else if (!SqlOpStr.OP_IS_NULL.equals(op) && !SqlOpStr.OP_NOT_NULL.equals(op)) {
            MybatisUtil.assertNotEmpty("parameter of " + field, value);
            if (value.length != 1) {
                throw new RuntimeException("The number of parameters[" + field + "] must be one.");
            }
        }
    }
}