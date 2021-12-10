package cn.org.atool.fluent.form.setter;

import java.util.Arrays;
import java.util.List;

/**
 * sql条件比较操作符常量
 *
 * @author wudarui
 */
public interface FormSqlOp {
    String OP_GT = "GT";

    String OP_GE = "GE";

    String OP_NE = "NE";

    String OP_EQ = "EQ";

    String OP_LE = "LE";

    String OP_LT = "LT";

    String OP_LIKE = "LIKE";

    String OP_LIKE_LEFT = "LIKE_LEFT";

    String OP_LIKE_RIGHT = "LIKE_RIGHT";

    String OP_NOT_LIKE = "NOT_LIKE";

    String OP_BETWEEN = "BETWEEN";

    String OP_NOT_BETWEEN = "NOT_BETWEEN";

    String OP_IN = "IN";

    String OP_NOT_IN = "NOT_IN";

    String OP_IS_NULL = "IS_NULL";

    String OP_NOT_NULL = "NOT_NULL";

    List<String> ALL_OP = Arrays.asList(
        OP_GT, OP_GE, OP_EQ, OP_NE, OP_LE, OP_LT,
        OP_LIKE, OP_LIKE_LEFT, OP_NOT_LIKE, OP_BETWEEN, OP_NOT_BETWEEN,
        OP_IN, OP_NOT_IN, OP_IS_NULL, OP_NOT_NULL
    );
}