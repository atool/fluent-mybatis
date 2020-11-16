package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.IEntity;
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

    /**
     * 分页查询数据
     *
     * @param clazz
     * @param <E>
     * @return
     */
    public <E extends IEntity, P extends IPagedList<E>> P paged(Class<E> clazz) {
        return (P) FormHelper.paged(clazz, this);
    }
}