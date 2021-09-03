package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRef;
import cn.org.atool.fluent.mybatis.base.crud.BaseFormSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.mapper.QueryExecutor;
import cn.org.atool.fluent.mybatis.functions.FormFunction;
import cn.org.atool.fluent.mybatis.utility.FormHelper;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单表单查询设置
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes", "unchecked"})
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
    private Serializable nextId;
    /**
     * 查询一页的数量
     */
    private int pageSize = 1;

    public <E extends IEntity> QueryExecutor<E> to(Class<E> entityClass) {
        IRichMapper mapper = IRef.mapper(entityClass);
        IQuery query = FormHelper.toQuery(entityClass, this);
        return new QueryExecutor(mapper, query);
    }

    public <E extends IEntity> IQuery<E> query(Class<E> entityClass) {
        return FormHelper.toQuery(entityClass, this);
    }

    public <E extends IEntity, S extends BaseFormSetter> IFormApply<E, S>
    add(FormFunction<E, S> apply, Object value) {
        return apply.apply(value, this);
    }
}