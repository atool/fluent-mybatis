package cn.org.atool.fluent.mybatis.base.impl;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IEntityMapper;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;

import java.util.List;

/**
 * 分页查询工具类
 *
 * @author darui.wu
 */
public class PagedHelper {
    /**
     * 标准分页查询
     *
     * @param mapper
     * @param query
     * @return
     */
    public static <E extends IEntity> StdPagedList<E> stdPagedEntity(IEntityMapper<E> mapper, IQuery<E, ?> query) {
        int total = mapper.countNoLimit(query);
        List list = mapper.listEntity(query);
        return new StdPagedList<>(total, list);
    }

    /**
     * 按tag分页查询
     *
     * @param mapper
     * @param query
     * @param <E>
     * @return
     */
    public static <E extends IEntity> TagPagedList<E> tagPagedEntity(IEntityMapper<E> mapper, IQuery<E, ?> query) {
        int size = validateTagPaged(query);
        query.limit(size + 1);
        List<E> list = mapper.listEntity(query);
        E next = null;
        if (list.size() > size) {
            next = list.remove(size);
        }
        return new TagPagedList<>(list, next);
    }


    /**
     * 校验marker方式分页的分页参数合法性
     *
     * @param query 查询条件
     * @return 最大查询数
     */
    public static int validateTagPaged(IQuery query) {
        PagedOffset paged = query.getWrapperData().getPaged();
        if (paged == null) {
            throw new FluentMybatisException("Paged parameter not set");
        }
        if (paged.getOffset() != 0) {
            throw new FluentMybatisException("The offset of TagList should from zero, please use method: limit(size) or limit(0, size) .");
        }
        return paged.getLimit();
    }
}