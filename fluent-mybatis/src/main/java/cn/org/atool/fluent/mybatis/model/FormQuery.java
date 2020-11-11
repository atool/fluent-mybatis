package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.EntityRefs;
import cn.org.atool.fluent.mybatis.base.IEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.model.FormItemOp.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotEmpty;

/**
 * 简单表单查询设置
 *
 * @author darui.wu
 */
@Data
@Accessors(chain = true)
public class FormQuery implements Serializable {
    private static final long serialVersionUID = 7702717917894301362L;

    /**
     * 条件项列表
     */
    private List<FormItem> items = new ArrayList<>();
    /**
     * 标准分页时, 当前页码
     */
    private Integer currPage;
    /**
     * Tag分页时, 当前页id值
     */
    private String nextId;
    /**
     * 查询一页的数量
     */
    private Integer pageSize;

    /**
     * 分页查询数据
     *
     * @param clazz
     * @param <E>
     * @return
     */
    public <E extends IEntity> IPagedList<E> paged(Class<E> clazz) {
        return EntityRefs.paged(clazz, this);
    }

    public FormQuery addCondition(String key, String op, String value) {
        this.items.add(new FormItem(key, op, value));
        return this;
    }

    public FormQuery addEq(String key, String value) {
        return this.addCondition(key, OP_EQ, value);
    }

    public FormQuery addGt(String key, String value) {
        return this.addCondition(key, OP_GT, value);
    }

    public FormQuery addGe(String key, String value) {
        return this.addCondition(key, OP_GE, value);
    }

    public FormQuery addLt(String key, String value) {
        return this.addCondition(key, OP_LT, value);
    }

    public FormQuery addLe(String key, String value) {
        return this.addCondition(key, OP_LE, value);
    }

    public FormQuery addLike(String key, String value) {
        return this.addCondition(key, OP_LIKE, value);
    }

    public FormQuery addNotLike(String key, String value) {
        return this.addCondition(key, OP_NOT_LIKE, value);
    }

    public FormQuery addBetween(String key, String min, String max) {
        assertNotBlank("min", min);
        assertNotBlank("max", max);
        return this.addCondition(key, OP_BETWEEN, this.joining(min, max));
    }

    public FormQuery addNotBetween(String key, String min, String max) {
        assertNotBlank("min", min);
        assertNotBlank("max", max);
        return this.addCondition(key, OP_NOT_BETWEEN, this.joining(min, max));
    }

    public FormQuery addIn(String key, String... items) {
        assertNotEmpty("items", items);
        return this.addCondition(key, OP_BETWEEN, this.joining(items));
    }

    public FormQuery addNotIn(String key, String... items) {
        assertNotEmpty("items", items);
        return this.addCondition(key, OP_NOT_IN, this.joining(items));
    }

    public FormQuery addIsNull(String key) {
        return this.addCondition(key, OP_IS_NULL, null);
    }

    public FormQuery addNotNull(String key) {
        return this.addCondition(key, OP_NOT_NULL, null);
    }

    private String joining(String... values) {
        for (int index = 0; index < values.length; index++) {
            assertNotBlank("item[" + index + "]", values[index]);
        }
        return "[" + String.join(", ", values) + "]";
    }
}