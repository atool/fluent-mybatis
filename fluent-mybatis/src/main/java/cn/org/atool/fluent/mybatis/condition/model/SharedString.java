package cn.org.atool.fluent.mybatis.condition.model;

import java.io.Serializable;

/**
 * 共享查询字段
 *
 * @author darui.wu
 */
public class SharedString implements Serializable {
    private static final long serialVersionUID = -1536422416594422874L;

    /**
     * 共享的 string 值
     */
    private String stringValue;

    public SharedString(String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * 追加到字符末尾
     *
     * @param suffix
     * @return
     */
    public SharedString append(String suffix) {
        if (stringValue == null) {
            this.stringValue = suffix;
        } else {
            this.stringValue = this.stringValue + suffix;
        }
        return this;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }

    /**
     * SharedString 里是 ""
     */
    public static SharedString emptyString() {
        return new SharedString(StrConstant.EMPTY);
    }

    public static SharedString str(String stringValue) {
        return new SharedString(stringValue);
    }
}