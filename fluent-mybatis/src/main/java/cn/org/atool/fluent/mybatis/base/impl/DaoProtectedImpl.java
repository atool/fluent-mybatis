package cn.org.atool.fluent.mybatis.base.impl;

import cn.org.atool.fluent.mybatis.base.*;
import cn.org.atool.fluent.mybatis.base.model.MarkerList;
import cn.org.atool.fluent.mybatis.base.model.PagedList;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;

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
    public int deleteBy(IQuery query) {
        return this.mapper().delete(query);
    }

    @Override
    public int updateBy(IUpdate update) {
        return this.mapper().updateBy(update);
    }

    @Override
    public List<E> listEntity(IQuery query) {
        return this.mapper().listEntity(query);
    }

    @Override
    public <F> List<F> listObjs(IQuery query, Function<E, F> converter) {
        List<E> list = this.mapper().listEntity(query);
        return this.toPoJoList(list, converter);
    }

    @Override
    public List<Map<String, Object>> listMaps(IQuery query) {
        return this.listPoJos(query, null);
    }

    @Override
    public <POJO> List<POJO> listPoJos(IQuery query, Function<Map<String, Object>, POJO> converter) {
        List<Map<String, Object>> list = this.mapper().listMaps(query);
        return this.toPoJoList(list, converter);
    }

    @Override
    public PagedList<E> pagedEntity(IQuery query) {
        int total = this.mapper().countNoLimit(query);
        List<E> list = this.mapper().listEntity(query);
        return new PagedList<>(total, list);
    }

    @Override
    public PagedList<Map<String, Object>> pagedMaps(IQuery query) {
        int total = this.mapper().countNoLimit(query);
        List<Map<String, Object>> list = this.mapper().listMaps(query);
        return new PagedList<>(total, list);
    }

    @Override
    public <POJO> PagedList<POJO> pagedPoJos(IQuery query, Function<Map<String, Object>, POJO> converter) {
        PagedList<Map<String, Object>> paged = this.pagedMaps(query);
        if (converter == null || paged == null || paged.getData() == null) {
            return (PagedList<POJO>) paged;
        } else {
            List<POJO> list = this.toPoJoList(paged.getData(), converter);
            return new PagedList<>(paged.getTotal(), list);
        }
    }

    @Override
    public MarkerList<E> markerPagedEntity(IQuery query) {
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
    public MarkerList<Map<String, Object>> markerPagedMaps(IQuery query) {
        int size = this.validateMarkerPaged(query);
        query.limit(size + 1);
        List list = this.mapper().listMaps(query);
        Map next = null;
        if (list.size() > size) {
            next = (Map) list.remove(size);
        }
        return new MarkerList<>(list, next);
    }

    @Override
    public <POJO> MarkerList<POJO> markerPagedPoJos(IQuery query, Function<Map<String, Object>, POJO> converter) {
        MarkerList<Map<String, Object>> paged = this.markerPagedMaps(query);
        List<POJO> list = this.toPoJoList(paged.getData(), converter);
        POJO next = this.toPoJo(paged.getNext(), converter);
        return new MarkerList<>(list, next);
    }

    @Override
    public E findOne(IQuery query) {
        query.limit(1);
        return this.mapper().findOne(query);
    }

    @Override
    public <F> F findOne(IQuery query, Function<E, F> converter) {
        E obj = this.findOne(query);
        return this.toPoJo(obj, converter);
    }

    @Override
    public int count(IQuery query) {
        return this.mapper().count(query);
    }

    /**
     * =======================================
     * =======================================
     */


    /**
     * ===============================================================
     *                           内部工具类方法
     * ===============================================================
     */
    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param map       map对象
     * @param converter 转换函数
     * @param <POJO>    PoJo类型
     * @return 转换后的对象
     */
    private <ME, POJO> POJO toPoJo(ME map, Function<ME, POJO> converter) {
        return map == null ? null : converter.apply(map);
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param list      map对象列表
     * @param converter 转换函数
     * @param <POJO>    PoJo类型
     * @return 转换后的对象列表
     */
    private <ME, POJO> List<POJO> toPoJoList(List<ME> list, Function<ME, POJO> converter) {
        return list == null ? null : list.stream().map(map -> toPoJo(map, converter)).collect(toList());
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