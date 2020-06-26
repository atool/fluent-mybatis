package cn.org.atool.fluent.mybatis.base.impl;

import cn.org.atool.fluent.mybatis.base.*;
import cn.org.atool.fluent.mybatis.base.model.MarkerList;
import cn.org.atool.fluent.mybatis.base.model.PagedList;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * DaoProtectedImpl: Dao Protected保护方法
 * 只允许在子类中调用，不暴露给Service等外部直接访问
 * <p>
 * ！！！！！！！！！！！！
 * 理由：
 * 不希望在Service中直接构造Query, Update等具体查询条件, 希望对外接口是语义化的
 * ！！！！！！！！！！！！
 *
 * @param <E> 实体类
 * @param <Q> 查询器
 * @param <U> 更新器
 * @author darui.wu
 * @create 2020/6/24 10:23 上午
 */
public abstract class DaoProtectedImpl<E extends IEntity, Q extends IQuery<E, Q>, U extends IUpdate<E, U, Q>>
    implements IBaseDao<E, Q, U>, IDaoProtected<E, Q, U> {

    @Override
    public int deleteByQuery(IQuery query) {
        return this.mapper().delete(query);
    }

    @Override
    public int update(IUpdate update) {
        return this.mapper().updateBy(update);
    }

    @Override
    public int count(IQuery query) {
        return this.mapper().count(query);
    }

    @Override
    public List<Map> selectObjs(IQuery query) {
        return this.selectObjs(query, null);
    }

    @Override
    public <F> List<F> selectObjs(IQuery query, Function<Map<String, Object>, F> function) {
        List<Map<String, Object>> list = this.mapper().list(query);
        if (function == null) {
            return (List<F>) list;
        } else {
            return list.stream()
                // mybatis有个bug，当所有字段值为null时, 不会返回Map对象, 而是返回一个null
                .map(map -> map == null ? new HashMap<String, Object>() : map)
                .map(function::apply)
                .collect(toList());
        }
    }

    @Override
    public <F> F selectOne(IQuery query, Function<E, F> function) {
        query.limit(1);
        E obj = (E) this.mapper().findOne(query);
        return obj == null ? null : (F) function.apply(obj);
    }

    @Override
    public <F> List<F> selectFields(IQuery query, Function<E, F> function) {
        return this.mapper().listEntity(query).stream()
            .map(function::apply)
            .collect(toList());
    }

    @Override
    public E selectOne(IQuery query) {
        query.limit(1);
        return (E) this.mapper().findOne(query);
    }

    @Override
    public PagedList<E> selectPagedList(IQuery query) {
        int total = this.mapper().countNoLimit(query);
        List<E> list = this.mapper().listEntity(query);
        return new PagedList<>(total, list);
    }

    @Override
    public MarkerList<E> selectMarkerList(IQuery query) {
        int size = this.validateMarkerPaged(query);
        query.limit(size + 1);
        List<E> list = this.mapper().listEntity(query);
        E next = null;
        if (list.size() > size) {
            next = list.remove(size);
        }
        return new MarkerList<>(list, next);
    }

    @Override
    public List<E> selectList(IQuery query) {
        return this.mapper().listEntity(query);
    }

    @Override
    public PagedList<Map> selectPagedMaps(IQuery query) {
        int total = this.mapper().countNoLimit(query);
        List list = this.mapper().list(query);
        return new PagedList<Map>(total, list);
    }

    @Override
    public MarkerList<Map> selectMarkerMaps(IQuery query) {
        int size = this.validateMarkerPaged(query);
        query.limit(size + 1);
        List list = this.mapper().list(query);
        Map next = null;
        if (list.size() > size) {
            next = (Map) list.remove(size);
        }
        return new MarkerList<>(list, next);
    }

    /**
     * 校验marker方式分页的分页参数合法性
     *
     * @param query 查询条件
     * @return 最大查询数
     */
    private int validateMarkerPaged(IQuery query) {
        PagedOffset paged = query.getWrapperData().getPaged();
        if (paged == null) {
            throw new FluentMybatisException("Paged parameter not set");
        }
        if (paged.getOffset() != 0) {
            throw new FluentMybatisException("The offset of MarkerList should from zero, please use method: limit(size) or limit(0, size) .");
        }
        return paged.getLimit();
    }
}