package cn.org.atool.fluent.mybatis.base.model.op;

import cn.org.atool.fluent.mybatis.base.model.ISqlOp;

/**
 * postgresql引擎,忽略大小写搜索英文
 */
public class ILike implements ISqlOp {

    ILike() {
    }

    @Override
    public String name() {
        return "ILIKE";
    }

    @Override
    public String getExpression() {
        return "ILIKE %s";
    }

    @Override
    public String getPlaceHolder() {
        return "ILIKE ?";
    }

    @Override
    public int getArgSize() {
        return 1;
    }
}
