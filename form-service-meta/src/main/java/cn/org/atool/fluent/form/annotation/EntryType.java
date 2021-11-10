package cn.org.atool.fluent.form.annotation;

import lombok.Getter;

/**
 * 表单字段类型
 *
 * @author darui.wu
 */
public enum EntryType {
    /**
     * 忽略字段
     */
    Ignore(false),
    /**
     * 更新字段
     */
    Update(false),
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
    PageSize(false),
    /**
     * 标准分页, 当前页码
     */
    CurrPage(false),
    /**
     * Tag分页, 主键值 >= tag
     */
    PagedTag(false),
    /**
     * order by 字段, 必须是布尔值(boolean, Boolean)
     * null:  表示忽略
     * true:  正序排序
     * false: 逆序排序
     */
    OrderBy(false),
    /**
     * 表单项
     */
    Form(false);

    /**
     * 是否where条件项
     */
    @Getter
    private final boolean isWhere;

    EntryType() {
        this.isWhere = true;
    }

    EntryType(boolean isWhere) {
        this.isWhere = isWhere;
    }
}