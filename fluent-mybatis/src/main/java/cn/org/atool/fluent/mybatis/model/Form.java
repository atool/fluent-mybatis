package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.FormSetter;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IQuery;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * 简单表单查询设置
 *
 * @author darui.wu
 */
@Data
@Accessors(chain = true)
public class Form implements Serializable {
    private static final long serialVersionUID = 7702717917894301362L;

    @Getter(AccessLevel.NONE)
    public final FormItemAdder add = new FormItemAdder(this);

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
    private int pageSize = 1;

    public static <E extends IEntity, C extends FormSetter<IFormQuery<E, C>>> IFormQuery<E, C>
    by(Class<C> setter, E entity) {
        assertNotNull("entity", entity);
        assertNotNull("column setter", setter);

        IQuery query = IRefs.instance().defaultQuery(entity.getClass());
        return new FormQuery(entity, query, setter);
    }

    public static <E extends IEntity, C extends FormSetter<IFormQuery<E, C>>> IFormQuery<E, C>
    by(Class<C> setter,Class<E> entityClass, Map form) {
        assertNotNull("form", form);
        assertNotNull("entityClass", entityClass);
        assertNotNull("column setter", setter);

        IQuery query = IRefs.instance().defaultQuery(entityClass);
        return new FormQuery(entityClass, query,form, setter);
    }

    /**
     * 分页查询数据
     *
     * @param clazz
     * @param <E>
     * @return
     */
    public <E extends IEntity, P extends IPagedList<E>> P paged(Class<E> clazz) {
        return (P) IRefs.paged(clazz, this);
    }
}