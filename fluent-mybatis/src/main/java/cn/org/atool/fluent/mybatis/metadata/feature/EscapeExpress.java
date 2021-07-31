package cn.org.atool.fluent.mybatis.metadata.feature;

import static cn.org.atool.fluent.mybatis.If.notBlank;

/**
 * 数据库反义符定义
 *
 * @author wudarui
 */
@SuppressWarnings("unused")
public class EscapeExpress {
    /**
     * 前缀
     */
    private final String prefix;
    /**
     * 后缀
     */
    private final String suffix;

    public EscapeExpress(String wrapper) {
        int index = wrapper.indexOf('?');
        if (index < 0) {
            throw new IllegalArgumentException("illegal antisense expression:" + wrapper);
        }
        this.prefix = wrapper.substring(0, index);
        this.suffix = wrapper.substring(index + 1);
    }

    public String wrap(String column) {
        if (notBlank(prefix) && notBlank(suffix)) {
            return prefix + column + suffix;
        } else {
            return column;
        }
    }

    /**
     * 去掉转义符
     *
     * @param column 可能带转义符的字段名称
     * @return 去掉转义符后的名称
     */
    public String unwrap(String column) {
        int len = column.length();
        if (notBlank(prefix) && notBlank(suffix) &&
            column.startsWith(prefix) && column.endsWith(suffix)) {
            return column.substring(prefix.length(), len - suffix.length());
        } else {
            return column;
        }
    }

    /**
     * 无反义处理
     */
    public final static EscapeExpress NONE_ESCAPE = new EscapeExpress("?");
    /**
     * 反引号反义处理
     */
    public final static EscapeExpress BACK_ESCAPE = new EscapeExpress("`?`");
    /**
     * 双引号反义处理
     */
    public final static EscapeExpress D_QUOTATION_ESCAPE = new EscapeExpress("\"?\"");
    /**
     * 单引号反义处理
     */
    public final static EscapeExpress S_QUOTATION_ESCAPE = new EscapeExpress("'?'");
    /**
     * 方括号反义处理
     */
    public final static EscapeExpress SQUARE_BRACKETS_ESCAPE = new EscapeExpress("[?]");
}