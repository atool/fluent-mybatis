package cn.org.atool.fluent.mybatis.base.impl;

import cn.org.atool.fluent.mybatis.base.*;
import cn.org.atool.fluent.mybatis.base.model.MarkerList;
import cn.org.atool.fluent.mybatis.base.model.PagedList;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
 * @author darui.wu
 * @create 2020/6/24 10:23 上午
 */
public abstract class DaoProtectedImpl<E extends IEntity>
    implements IMapperDao<E>, IDaoProtected<E> {

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
    public List<Map<String, Object>> listMaps(IQuery query) {
        return this.listPoJos(query, null);
    }

    @Override
    public <POJO> List<POJO> listPoJos(IQuery query, Function<Map<String, Object>, POJO> converter) {
        List<Map<String, Object>> list = this.mapper().listMaps(query);
        return this.toPoJoList(list, converter);
    }

    @Override
    public <POJO> List<POJO> listPoJos(Class<POJO> clazz, IQuery query) {
        List<Map<String, Object>> list = this.mapper().listMaps(query);
        return this.toPoJoList(clazz, list);
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
    public <POJO> PagedList<POJO> pagedPoJos(Class<POJO> klass, IQuery query) {
        PagedList<Map<String, Object>> paged = this.pagedMaps(query);
        List<POJO> list = this.toPoJoList(klass, paged.getData());
        return new PagedList<>(paged.getTotal(), list);
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
    public <POJO> MarkerList<POJO> markerPagedPoJos(Class<POJO> klass, IQuery query) {
        MarkerList<Map<String, Object>> paged = this.markerPagedMaps(query);
        List<POJO> list = this.toPoJoList(klass, paged.getData());
        POJO next = this.toPoJo(klass, paged.getNext());
        return new MarkerList<>(list, next);
    }

    @Override
    public Optional<E> findOne(IQuery query) {
        query.limit(1);
        E one = this.mapper().findOne(query);
        return Optional.ofNullable(one);
    }

    @Override
    public Optional<Map<String, Object>> findOneResult(IQuery query) {
        query.limit(1);
        List<Map<String, Object>> list = this.listMaps(query);
        Map<String, Object> map = list == null || list.size() == 0 ? null : list.get(0);
        return Optional.ofNullable(map);
    }

    @Override
    public <F> Optional<F> findOnePoJo(IQuery query, Function<Map<String, Object>, F> converter) {
        Optional<Map<String, Object>> optional = this.findOneResult(query);
        return optional.map(m -> this.toPoJo(m, converter));
    }

    @Override
    public <POJO> Optional<POJO> findOnePoJo(Class<POJO> klass, IQuery query) {
        Optional<Map<String, Object>> optional = this.findOneResult(query);
        return optional.map(m -> this.toPoJo(klass, m));
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
     * @param map       map或entity对象
     * @param converter 转换函数
     * @param <POJO>    PoJo类型
     * @return 转换后的对象
     */
    private <POJO> POJO toPoJo(Map<String, Object> map, Function<Map<String, Object>, POJO> converter) {
        return map == null ? null : converter.apply(map);
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param klass  POJO类型
     * @param map    map对象
     * @param <POJO> PoJo类型
     * @return 根据Map值设置后的对象
     */
    private <POJO> POJO toPoJo(Class<POJO> klass, Map<String, Object> map) {
        try {
            POJO target = klass.newInstance();
            MetaObject metaObject = MetaObject.forObject(target, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String name = MybatisUtil.underlineToCamel(entry.getKey(), false);
                metaObject.setValue(name, entry.getValue());
            }
            return target;
        } catch (Exception e) {
            throw new RuntimeException("convert map to object[type=" + klass.getName() + "] error: " + e.getMessage(), e);
        }
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param list      Map或Entity对象列表
     * @param converter 转换函数
     * @param <POJO>    PoJo类型
     * @return 转换后的对象列表
     */
    private <POJO> List<POJO> toPoJoList(List<Map<String, Object>> list, Function<Map<String, Object>, POJO> converter) {
        return list == null ? null : list.stream().map(map -> toPoJo(map, converter)).collect(toList());
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param klass  POJO类型
     * @param list   map对象列表
     * @param <POJO> POJO类型
     * @return POJO实例列表
     */
    private <POJO> List<POJO> toPoJoList(Class<POJO> klass, List<Map<String, Object>> list) {
        return list == null ? null : list.stream().map(map -> toPoJo(klass, map)).collect(toList());
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