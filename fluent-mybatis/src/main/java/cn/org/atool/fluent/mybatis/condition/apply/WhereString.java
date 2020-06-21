package cn.org.atool.fluent.mybatis.condition.apply;

import cn.org.atool.fluent.mybatis.condition.base.BaseWhere;
import cn.org.atool.fluent.mybatis.condition.base.Wrapper;
import cn.org.atool.fluent.mybatis.condition.model.SqlLike;
import cn.org.atool.fluent.mybatis.interfaces.IEntity;
import cn.org.atool.fluent.mybatis.interfaces.IQuery;

import static cn.org.atool.fluent.mybatis.condition.model.SqlOp.LIKE;
import static cn.org.atool.fluent.mybatis.condition.model.SqlOp.NOT_LIKE;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * AndString
 *
 * @param <E> 对应的实体类
 * @param <W> 更新器或查询器
 * @param <Q> 对应的嵌套查询器
 * @author darui.wu
 */
public class WhereString<E extends IEntity, W extends Wrapper<E, W, Q>, Q extends IQuery<E, Q>> extends WhereObject<E, String, W, Q> {
    public WhereString(BaseWhere queryAnd, String column, String property) {
        super(queryAnd, column, property);
    }

    /**
     * eq_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public W eq_IfNotBlank(String value) {
        return super.eq(isNotEmpty(value), value);
    }

    /**
     * ne_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public W ne_IfNotBlank(String value) {
        return super.ne(isNotEmpty(value), value);
    }

    /**
     * gt_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public W gt_IfNotBlank(String value) {
        return super.gt(isNotEmpty(value), value);
    }

    /**
     * ge_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public W ge_IfNotBlank(String value) {
        return super.ge(isNotEmpty(value), value);
    }

    /**
     * lt_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public W lt_IfNotBlank(String value) {
        return super.lt(isNotEmpty(value), value);
    }

    /**
     * le_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public W le_IfNotBlank(String value) {
        return super.le(isNotEmpty(value), value);
    }

    //like

    /**
     * like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    public W like(String value) {
        assertNotBlank(property, value);
        return this.wrapper().apply(orAnd, column, LIKE, SqlLike.like(value));
    }

    /**
     * like '%value%'
     *
     * @param condition 成立条件
     * @param value     条件值
     * @return self
     */
    public W like(boolean condition, String value) {
        return condition ? this.like(value) : this.wrapper();
    }

    /**
     * value不为空时, 执行 like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    public W like_IfNotBlank(String value) {
        return this.like(isNotEmpty(value), value);
    }

    //not like

    /**
     * not like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    public W notLike(String value) {
        assertNotBlank(property, value);
        return this.wrapper().apply(orAnd, column, NOT_LIKE, SqlLike.like(value));
    }

    /**
     * not like '%value%'
     *
     * @param condition 成立条件
     * @param value     条件值
     * @return self
     */
    public W notLike(boolean condition, String value) {
        return condition ? this.notLike(value) : this.wrapper();
    }

    /**
     * not like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    public W notLike_IfNotBlank(String value) {
        return this.notLike(isNotEmpty(value), value);
    }

    //like left

    /**
     * like '%value'
     *
     * @param value
     * @return
     */
    public W likeLeft(String value) {
        assertNotBlank(property, value);
        return this.wrapper().apply(orAnd, column, LIKE, SqlLike.left(value));
    }

    public W likeLeft(boolean condition, String value) {
        return condition ? this.likeLeft(value) : this.wrapper();
    }


    public W likeLeft_IfNotBlank(String value) {
        return this.likeLeft(isNotEmpty(value), value);
    }

    //like right

    /**
     * like 'value%'
     *
     * @param value
     * @return
     */
    public W likeRight(String value) {
        assertNotBlank(property, value);
        return this.wrapper().apply(orAnd, column, LIKE, SqlLike.right(value));
    }

    public W likeRight(boolean condition, String value) {
        return condition ? this.likeRight(value) : this.wrapper();
    }

    public W likeRight_IfNotBlank(String value) {
        return this.likeRight(isNotEmpty(value), value);
    }
}