package cn.org.atool.fluent.form;

/**
 * 表单字段类型
 *
 * @author darui.wu
 */
public enum ItemType {
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