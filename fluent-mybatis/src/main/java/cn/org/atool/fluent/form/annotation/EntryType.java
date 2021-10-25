package cn.org.atool.fluent.form.annotation;

/**
 * 表单字段类型
 *
 * @author darui.wu
 */
public enum EntryType {
    /**
     * 更新或插入的字段
     */
    UPDATE,
    EQ,
    GT,
    GE,
    LT,
    LE,
    NE,
    IN,
    LIKE,
    LIKE_LEFT,
    LIKE_RIGHT,
    BETWEEN
}