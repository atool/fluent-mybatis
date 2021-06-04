package cn.org.atool.fluent.mybatis.base.model.op;

import cn.org.atool.fluent.mybatis.base.model.ISqlOp;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 扩展操作符
 *
 * @author wudarui
 */
public class SqlOps {
    /**
     * 扩展操作符
     */
    private final static List<ISqlOp> EXT_OPS = new ArrayList<>();
    /**
     * postgresql引擎,忽略大小写搜索英文
     */
    public final static PostgreSqlILike PG_ILike = new PostgreSqlILike();

    static {
        register(PG_ILike);
    }

    /**
     * 注册新的自定义操作符
     *
     * @param sqlOp
     */
    public static void register(ISqlOp sqlOp) {
        EXT_OPS.add(sqlOp);
    }

    /**
     * 返回匹配的操作符实例
     *
     * @param op
     * @return
     */
    public static ISqlOp get(String op) {
        try {
            return SqlOp.valueOf(op);
        } catch (IllegalArgumentException e) {
            for (ISqlOp item : EXT_OPS) {
                if (Objects.equals(item.name(), op)) {
                    return item;
                }
            }
            throw e;
        }
    }
}
