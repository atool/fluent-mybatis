package cn.org.atool.fluent.mybatis.base.mapper;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.functions.MapFunction;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 查询条件执行器
 *
 * @param <E> 实例类型
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class QueryExecutor<E extends IEntity> {
    private final IRichMapper mapper;

    private final IQuery query;

    public QueryExecutor(IRichMapper mapper, IQuery query) {
        this.mapper = mapper;
        this.query = query;
    }

    public Optional<E> findOne() {
        return Optional.ofNullable((E) this.mapper.findOne(this.query));
    }

    public <POJO> Optional<POJO> findOne(Function<E, POJO> mapFunction) {
        E entity = (E) this.mapper.findOne(this.query);
        POJO value = entity == null ? null : mapFunction.apply(entity);
        return Optional.ofNullable(value);
    }

    public <POJO> Optional<POJO> findOne(Class<POJO> clazz) {
        return this.mapper.findOne(clazz, this.query);
    }

    public Optional<Map> findOneMap() {
        return this.mapper.findOneMap(this.query);
    }

    public <POJO> Optional<POJO> findOneMap(MapFunction<POJO> mapFunction) {
        return this.mapper.findOne(this.query, mapFunction);
    }

    public List<E> listEntity() {
        return this.mapper.listEntity(this.query);
    }

    public List<Map> listMaps() {
        return this.mapper.listMaps(this.query);
    }

    public <O> List<O> listObjs() {
        return this.mapper.listObjs(this.query);
    }

    public <POJO> List<POJO> listPoJo(MapFunction<POJO> mapFunction) {
        return this.mapper.listPoJos(this.query, mapFunction);
    }

    public <POJO> List<POJO> listPoJo(Class<POJO> clazz) {
        return this.mapper.listPoJos(clazz, this.query);
    }

    public StdPagedList<E> stdPagedEntity() {
        return this.mapper.stdPagedEntity(this.query);
    }

    public StdPagedList<Map<String, Object>> stdPagedMap() {
        return this.mapper.stdPagedMap(this.query);
    }

    public <POJO> StdPagedList<POJO> stdPagedPoJo(MapFunction<POJO> mapFunction) {
        return this.mapper.stdPagedPoJo(this.query, mapFunction);
    }

    public <POJO> StdPagedList<POJO> stdPagedPoJo(Class<POJO> clazz) {
        return this.mapper.stdPagedPoJo(clazz, this.query);
    }

    public TagPagedList<E> tagPagedEntity() {
        return this.mapper.tagPagedEntity(this.query);
    }

    public TagPagedList<Map<String, Object>> tagPagedMap() {
        return this.mapper.tagPagedMap(this.query);
    }

    public <POJO> TagPagedList<POJO> tagPagedPoJo(MapFunction<POJO> mapFunction) {
        return this.mapper.tagPagedPoJo(this.query, mapFunction);
    }

    public <POJO> TagPagedList<POJO> tagPagedPoJo(Class<POJO> clazz) {
        return this.mapper.tagPagedPoJo(clazz, this.query);
    }

    public int count() {
        return this.mapper.count(this.query);
    }

    public int countNoLimit() {
        return this.mapper.countNoLimit(this.query);
    }

    public int delete() {
        return this.mapper.delete(this.query);
    }

    public int insertSelect(String... fields) {
        return this.mapper.insertSelect(fields, this.query);
    }

    public int logicDelete() {
        return this.mapper.logicDelete(this.query);
    }
}