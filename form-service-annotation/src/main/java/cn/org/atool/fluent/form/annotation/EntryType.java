package cn.org.atool.fluent.form.annotation;

/**
 * 表单字段类型
 *
 * @author darui.wu
 */
public enum EntryType {
    /**
     * 忽略字段
     */
    Ignore,
    /**
     * 更新或插入的字段
     */
    Update,
    /**
     * 相等, 或save时赋值
     */
    EQ,
    /**
     * 大于
     */
    GT,
    /**
     * 大于等于
     */
    GE,
    /**
     * 小于
     */
    LT,
    /**
     * 小于等于
     */
    LE,
    /**
     * 不等于
     */
    NE,
    /**
     * IN (?, ?)
     */
    IN,
    /**
     * like %value%
     */
    Like,
    /**
     * like value%
     */
    LikeLeft,
    /**
     * like %value
     */
    LikeRight,
    /**
     * between ? and ?
     */
    Between,
    /**
     * 分页, 每页记录数
     */
    PageSize,
    /**
     * 标准分页, 当前页码
     */
    CurrPage,
    /**
     * Tag分页, 主键值 >= tag
     */
    PagedTag
}