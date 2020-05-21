package cn.org.atool.fluent.mybatis.condition;

/**
 * SQL like 枚举
 *
 * @author darui.wu
 */
public enum SqlLike {
    /**
     * %值
     */
    LEFT {
        @Override
        public String like(Object input) {
            return "%" + input;
        }
    },
    /**
     * 值%
     */
    RIGHT {
        @Override
        public String like(Object input) {
            return input + "%";
        }
    },
    /**
     * %值%
     */
    DEFAULT {
        @Override
        public String like(Object input) {
            return "%" + input + "%";
        }
    };

    public abstract String like(Object input);
}