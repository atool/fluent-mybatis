package cn.org.atool.fluent.mybatis.base.model;

import java.util.Arrays;
import java.util.List;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;

/**
 * sql条件比较操作符常量
 *
 * @author wudarui
 */
public interface SqlOpStr {

    String OP_GT = GT.name();

    String OP_GE = GE.name();

    String OP_NE = NE.name();

    String OP_EQ = EQ.name();

    String OP_LE = LE.name();

    String OP_LT = LT.name();

    String OP_LIKE = LIKE.name();

    String OP_NOT_LIKE = NOT_LIKE.name();

    String OP_BETWEEN = BETWEEN.name();

    String OP_NOT_BETWEEN = NOT_BETWEEN.name();

    String OP_IN = IN.name();

    String OP_NOT_IN = NOT_IN.name();

    String OP_IS_NULL = IS_NULL.name();

    String OP_NOT_NULL = NOT_NULL.name();

    List<String> ALL_OP = Arrays.asList(
        OP_GT, OP_GE, OP_EQ, OP_NE, OP_LE, OP_LT,
        OP_LIKE, OP_NOT_LIKE, OP_BETWEEN, OP_NOT_BETWEEN,
        OP_IN, OP_NOT_IN, OP_IS_NULL, OP_NOT_NULL
    );
}
